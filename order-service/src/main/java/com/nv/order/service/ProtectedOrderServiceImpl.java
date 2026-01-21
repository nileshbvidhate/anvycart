package com.nv.order.service;

import com.nv.order.client.CartFeignClient;
import com.nv.order.client.DeliveryFeignClient;
import com.nv.order.client.InventoryFeignClient;
import com.nv.order.client.PaymentFeignClient;
import com.nv.order.client.ProductFeignClient;
import com.nv.order.client.UserFeignClient;
import com.nv.order.dto.request.BuyNowOrderRequest;
import com.nv.order.dto.request.OrderCreateRequest;
import com.nv.order.dto.request.InventoryReleaseRequest;
import com.nv.order.dto.request.InventoryReserveRequest;
import com.nv.order.dto.request.OrderPaymentVerifyRequest;
import com.nv.order.dto.request.RazorpayOrderCreateRequest;
import com.nv.order.dto.response.CartItemResponse;
import com.nv.order.dto.response.CreateOrderResponse;
import com.nv.order.dto.response.OrderPaymentVerifyResponse;
import com.nv.order.dto.response.OrderResponse;
import com.nv.order.dto.response.ProductResponse;
import com.nv.order.dto.response.RazorpayOrderCreateResponse;
import com.nv.order.dto.response.RazorpayPaymentVerifyResponse;
import com.nv.order.entity.Order;
import com.nv.order.entity.OrderItem;
import com.nv.order.entity.OrderSource;
import com.nv.order.entity.OrderStatus;
import com.nv.order.exception.BadRequestException;
import com.nv.order.exception.CustomAccessDeniedException;
import com.nv.order.exception.ResourceNotFoundException;
import com.nv.order.mapper.OrderMapper;
import com.nv.order.repository.OrderRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

@Service
public class ProtectedOrderServiceImpl implements ProtectedOrderService {

	private final OrderRepository orderRepository;

	private final UserFeignClient userFeignClient;
	private final CartFeignClient cartFeignClient;
	private final ProductFeignClient productFeignClient;
	private final InventoryFeignClient inventoryFeignClient;
	private final PaymentFeignClient paymentFeignClient;
	private final DeliveryFeignClient deliveryFeignClient;
	private final InternalOrderService internalOrderService;

	private final OrderMapper orderMapper;

	private final String currency;

	public ProtectedOrderServiceImpl(OrderRepository orderRepository, UserFeignClient userFeignClient,
			CartFeignClient cartFeignClient, ProductFeignClient productFeignClient,
			InventoryFeignClient inventoryFeignClient, PaymentFeignClient paymentFeignClient, OrderMapper orderMapper,
			@Value("${payment.currency-type}") String currency, DeliveryFeignClient deliveryFeignClient,
			InternalOrderService internalOrderService) {

		this.orderRepository = orderRepository;
		this.userFeignClient = userFeignClient;
		this.cartFeignClient = cartFeignClient;
		this.productFeignClient = productFeignClient;
		this.inventoryFeignClient = inventoryFeignClient;
		this.paymentFeignClient = paymentFeignClient;
		this.deliveryFeignClient = deliveryFeignClient;
		this.orderMapper = orderMapper;
		this.currency = currency;
		this.internalOrderService = internalOrderService;
	}

	public BigDecimal calculateFinalPrice(BigDecimal basePrice, Integer discountPercentage) {

		if (discountPercentage == null || discountPercentage <= 0) {
			return basePrice.setScale(2, RoundingMode.HALF_UP);
		}

		if (discountPercentage >= 100) {
			return BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP);
		}

		BigDecimal discountRate = BigDecimal.valueOf(discountPercentage).divide(BigDecimal.valueOf(100), 4,
				RoundingMode.HALF_UP);

		return basePrice.multiply(BigDecimal.ONE.subtract(discountRate)).setScale(2, RoundingMode.HALF_UP);
	}

	@Transactional
	@Override
	public CreateOrderResponse createOrder(Long userId, OrderCreateRequest request) {

		// STEP 1: Validate address belongs to user

		userFeignClient.validateAddress(userId, request.getAddressId());

		// STEP 2: Fetch cart items
		List<CartItemResponse> cartItems = cartFeignClient.getCart(userId).getData();

		if (cartItems == null || cartItems.isEmpty()) {
			throw new BadRequestException("Cart is empty");
		}

		// STEP 3: Get the Product details from product service (Calculate price using
		// Product Service)
		BigDecimal totalAmount = BigDecimal.ZERO;

		List<OrderItem> orderItems = new ArrayList<>();

		for (CartItemResponse cartItem : cartItems) {

			ProductResponse product = productFeignClient.getProduct(cartItem.getProductId()).getData();

			if (!"ACTIVE".equals(product.getStatus())) {
				throw new BadRequestException("Product not available: " + cartItem.getProductId());
			}

			BigDecimal basePrice = product.getBasePrice();
			Integer discount = product.getDiscountPercentage();

			BigDecimal finalPrice = calculateFinalPrice(basePrice, discount);

			BigDecimal itemTotal = finalPrice.multiply(BigDecimal.valueOf(cartItem.getQuantity()));

			totalAmount = totalAmount.add(itemTotal);

			OrderItem item = OrderItem.builder().productId(cartItem.getProductId()).name(product.getName())
					.quantity(cartItem.getQuantity()).basePrice(basePrice).discountPercentage(discount)
					.finalPrice(finalPrice).build();

			orderItems.add(item);
		}

		// STEP 4: Reserve inventory for order
		for (CartItemResponse cartItem : cartItems) {

			InventoryReserveRequest inventoryReserveRequest = new InventoryReserveRequest();

			inventoryReserveRequest.setQuantity(cartItem.getQuantity());

			inventoryFeignClient.reserveStock(cartItem.getProductId(), inventoryReserveRequest);
		}

		// STEP 5: Create Order
		Order order = Order.builder().userId(userId).addressId(request.getAddressId()).totalAmount(totalAmount)
				.status(OrderStatus.PAYMENT_PENDING).orderSource(OrderSource.CART).build();

		// STEP 6: attach order items
		for (OrderItem item : orderItems) {
			item.setOrder(order);
		}

		order.setOrderItems(orderItems);

		// Save ONLY order (Because JPA cascade handles saves it)
		Order savedOrder = orderRepository.save(order);

		// STEP 7: Create a payment order calling the payment service
		RazorpayOrderCreateRequest razorpayOrderCreateRequest = new RazorpayOrderCreateRequest();

		razorpayOrderCreateRequest.setOrderId(savedOrder.getId());
		razorpayOrderCreateRequest.setAmount(savedOrder.getTotalAmount());
		razorpayOrderCreateRequest.setCurrency(currency);

		RazorpayOrderCreateResponse razorpayOrderCreateResponse = paymentFeignClient
				.createRazorpayOrder(razorpayOrderCreateRequest).getData();

		// STEP 8: Return response
		return orderMapper.toCreateOrderResponse(savedOrder, razorpayOrderCreateResponse);

	}

	@Transactional
	@Override
	public CreateOrderResponse retryPayment(Long orderId) {

		// 1. Fetch order
		Order order = orderRepository.findById(orderId)
				.orElseThrow(() -> new ResourceNotFoundException("Order not found"));

		// 2. Validate order state
		if (order.getStatus() == OrderStatus.PLACED) {
			throw new BadRequestException("Payment already completed");
		}

		if (order.getStatus() == OrderStatus.CANCELLED) {
			throw new BadRequestException("Order cannot be paid");
		}

		if (order.getStatus() != OrderStatus.PAYMENT_PENDING || order.getStatus() != OrderStatus.PAYMENT_PROCESSING) {
			throw new BadRequestException("Payment retry not allowed");
		}

		// 3. Call Payment Service
		RazorpayOrderCreateRequest request = new RazorpayOrderCreateRequest();

		request.setOrderId(order.getId());
		request.setAmount(order.getTotalAmount());
		request.setCurrency(currency);

		RazorpayOrderCreateResponse razorpayOrderCreateResponse = paymentFeignClient.createRazorpayOrder(request)
				.getData();

		// 4. Return response
		return orderMapper.toCreateOrderResponse(order, razorpayOrderCreateResponse);
	}

	@Override
	@Transactional
	public CreateOrderResponse buyNow(Long userId, BuyNowOrderRequest request) {

		// STEP 1: Validate address
		userFeignClient.validateAddress(userId, request.getAddressId());

		// STEP 2: Fetch product
		ProductResponse product = productFeignClient.getProduct(request.getProductId()).getData();

		if (product == null || !"ACTIVE".equals(product.getStatus())) {
			throw new BadRequestException("Product not available");
		}

		// STEP 3: Calculate pricing
		BigDecimal basePrice = product.getBasePrice();
		Integer discount = product.getDiscountPercentage();

		BigDecimal finalPrice = calculateFinalPrice(basePrice, discount);
		BigDecimal totalAmount = finalPrice.multiply(BigDecimal.valueOf(request.getQuantity()));

		// STEP 4: Reserve inventory
		InventoryReserveRequest inventoryReserveRequest = new InventoryReserveRequest();
		inventoryReserveRequest.setQuantity(request.getQuantity());

		inventoryFeignClient.reserveStock(request.getProductId(), inventoryReserveRequest);

		// STEP 5: Create Order
		Order order = Order.builder().userId(userId).addressId(request.getAddressId()).totalAmount(totalAmount)
				.status(OrderStatus.PAYMENT_PENDING).orderSource(OrderSource.BUY_NOW).build();

		// STEP 6: Create OrderItem
		OrderItem orderItem = OrderItem.builder().productId(request.getProductId()).name(product.getName())
				.quantity(request.getQuantity()).basePrice(basePrice).discountPercentage(discount)
				.finalPrice(finalPrice).build();

		orderItem.setOrder(order);
		order.setOrderItems(List.of(orderItem));

		Order savedOrder = orderRepository.save(order);

		// STEP 7: Create payment order
		RazorpayOrderCreateRequest razorpayOrderCreateRequest = new RazorpayOrderCreateRequest();

		razorpayOrderCreateRequest.setOrderId(savedOrder.getId());
		razorpayOrderCreateRequest.setAmount(savedOrder.getTotalAmount());
		razorpayOrderCreateRequest.setCurrency(currency);

		RazorpayOrderCreateResponse razorpayOrderCreateResponse = paymentFeignClient
				.createRazorpayOrder(razorpayOrderCreateRequest).getData();

		// STEP 8: Response
		return orderMapper.toCreateOrderResponse(savedOrder, razorpayOrderCreateResponse);
	}

	@Override
	@Transactional
	public OrderResponse getOrderById(Long userId, Long orderId) {

		Order order = orderRepository.findById(orderId)
				.orElseThrow(() -> new ResourceNotFoundException("Order not found"));

		if (!order.getUserId().equals(userId)) {
			throw new CustomAccessDeniedException("You are not allowed to access this order (Not belongs to you!!!)");
		}

		return orderMapper.toOrderResponse(order);
	}

	@Override
	@Transactional
	public List<OrderResponse> getOrders(Long userId) {

		List<Order> orders = orderRepository.findByUserIdOrderByCreatedAtDesc(userId);

		return orders.stream().map(orderMapper::toOrderResponse).toList();
	}

	@Override
	@Transactional
	public void cancelOrder(Long userId, Long orderId) {

		Order order = orderRepository.findById(orderId)
				.orElseThrow(() -> new ResourceNotFoundException("Order not found"));

//		PAYMENT_PENDING,
//	    PAYMENT_PROCESSING,
//	    PAYMENT_FAILED,
//	    PLACED,
//	    CONFIRMED,	
//	    SHIPPED,
//	    OUT_FOR_DELIVERY,
//	    DELIVERED,
//		CANCELLED

		// Ownership check
		if (!order.getUserId().equals(userId)) {
			throw new CustomAccessDeniedException("You are not allowed to cancel this order");
		}

		// Cancellation not allowed after delivery execution
		if (order.getStatus() == OrderStatus.OUT_FOR_DELIVERY || order.getStatus() == OrderStatus.DELIVERED) {

			throw new BadRequestException("Order cannot be cancelled at this stage");
		}

		// Prevent duplicate cancellation
		if (order.getStatus() == OrderStatus.CANCELLED) {
			throw new BadRequestException("Order is already cancelled");
		}

		// STEP 1: Cancel delivery if it exists
		if (order.getStatus() == OrderStatus.CONFIRMED || order.getStatus() == OrderStatus.SHIPPED) {

			deliveryFeignClient.cancelDelivery(order.getId());
		}

		// STEP 2: Update order status
		order.setStatus(OrderStatus.CANCELLED);

		// STEP 3: Release inventory
		for (OrderItem item : order.getOrderItems()) {

			InventoryReleaseRequest request = new InventoryReleaseRequest();
			request.setQuantity(item.getQuantity());

			inventoryFeignClient.releaseStock(item.getProductId(), request);
		}

		// Notes:
		// - Refund handled by Payment Service later
	}

	@Override
	public OrderPaymentVerifyResponse verifyPayment(Long orderId, OrderPaymentVerifyRequest request) {

		// 1. Fetch order
		Order order = orderRepository.findById(orderId)
				.orElseThrow(() -> new ResourceNotFoundException("Order not found"));

		// 2. Validate order state
		if (order.getStatus() != OrderStatus.PAYMENT_PENDING) {
			throw new BadRequestException("Payment verification not allowed");
		}

		// 3. Call Payment Service
		RazorpayPaymentVerifyResponse paymentResponse = paymentFeignClient.verifyPayment(request).getData();

		String paymentStatus = paymentResponse.getStatus();

		// 4. Handle payment status explicitly
		if ("AUTHORIZED".equals(paymentStatus)) {

			order.setStatus(OrderStatus.PAYMENT_PROCESSING);
			orderRepository.save(order);

			return new OrderPaymentVerifyResponse(OrderStatus.PAYMENT_PROCESSING.name());
		}

		if ("CAPTURED".equals(paymentStatus)) {

			internalOrderService.handlePaymentSuccess(orderId);

			return new OrderPaymentVerifyResponse(OrderStatus.PLACED.name());
		}

		order.setStatus(OrderStatus.PAYMENT_FAILED);
		orderRepository.save(order);

		return new OrderPaymentVerifyResponse(OrderStatus.PAYMENT_FAILED.name());
	}

}
