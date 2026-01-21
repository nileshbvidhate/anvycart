package com.nv.order.service;

import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.nv.order.entity.Order;
import com.nv.order.entity.OrderItem;
import com.nv.order.entity.OrderSource;
import com.nv.order.entity.OrderStatus;
import com.nv.order.exception.BadRequestException;
import com.nv.order.exception.ResourceNotFoundException;
import com.nv.order.repository.OrderRepository;
import com.nv.order.client.InventoryFeignClient;
import com.nv.order.client.CartFeignClient;
import com.nv.order.dto.request.InventoryConfirmRequest;
import com.nv.order.dto.request.InventoryReleaseRequest;
import com.nv.order.dto.response.InternalOrderResponse;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class InternalOrderServiceImpl implements InternalOrderService {

	private final OrderRepository orderRepository;
	private final InventoryFeignClient inventoryFeignClient;
	private final CartFeignClient cartFeignClient;

	@Override
	@Transactional
	public void handlePaymentSuccess(Long orderId) {

		Order order = orderRepository.findById(orderId)
				.orElseThrow(() -> new ResourceNotFoundException("Order not found"));

		if (order.getStatus() != OrderStatus.PLACED) {
			return;
		}

		// Validate state
		if (order.getStatus() != OrderStatus.PAYMENT_PENDING || order.getStatus() != OrderStatus.PAYMENT_PROCESSING) {
			throw new BadRequestException("Invalid order state for payment success");
		}

		// STEP 1: Update order status
		order.setStatus(OrderStatus.PLACED);

		// STEP 2: Confirm inventory
		List<OrderItem> items = order.getOrderItems();

		for (OrderItem item : items) {

			InventoryConfirmRequest request = new InventoryConfirmRequest();
			request.setQuantity(item.getQuantity());

			inventoryFeignClient.confirmStock(item.getProductId(), request);
		}

		// STEP 3: Clear cart (cart-based orders only)
		if (order.getOrderSource() == OrderSource.CART) {
			cartFeignClient.clearCart(order.getUserId());
		}

		// Order is updated automatically due to @Transactional
	}

	@Override
	@Transactional
	public void handlePaymentFailed(Long orderId) {

		Order order = orderRepository.findById(orderId)
				.orElseThrow(() -> new ResourceNotFoundException("Order not found"));

		if (order.getStatus() != OrderStatus.PAYMENT_FAILED) {
			return;
		}

		// Validate state
		if (order.getStatus() != OrderStatus.PAYMENT_PENDING || order.getStatus() != OrderStatus.PAYMENT_PROCESSING) {
			throw new BadRequestException("Invalid order state for payment failure");
		}

		// STEP 1: Update order status
		order.setStatus(OrderStatus.CANCELLED);

		// STEP 2: Release inventory
		for (OrderItem item : order.getOrderItems()) {

			InventoryReleaseRequest request = new InventoryReleaseRequest();
			request.setQuantity(item.getQuantity());

			inventoryFeignClient.releaseStock(item.getProductId(), request);
		}

		// NO cart clearing here
	}

	@Transactional
	@Override
	public InternalOrderResponse getOrderById(Long orderId) {

		Order order = orderRepository.findById(orderId)
				.orElseThrow(() -> new ResourceNotFoundException("Order not found"));

		return InternalOrderResponse.builder().orderId(order.getId()).userId(order.getUserId())
				.addressId(order.getAddressId()).build();

	}

	@Override
	@Transactional
	public void markOrderShipped(Long orderId) {

		Order order = orderRepository.findById(orderId)
				.orElseThrow(() -> new ResourceNotFoundException("Order not found"));

		if (order.getStatus() != OrderStatus.CONFIRMED) {
			throw new BadRequestException("Order cannot be marked as shipped from status: " + order.getStatus());
		}

		order.setStatus(OrderStatus.SHIPPED);
	}

	@Override
	@Transactional
	public void markOutForDelivery(Long orderId) {

		Order order = orderRepository.findById(orderId)
				.orElseThrow(() -> new ResourceNotFoundException("Order not found"));

		if (order.getStatus() != OrderStatus.SHIPPED) {
			throw new BadRequestException("Order cannot be marked out for delivery from status: " + order.getStatus());
		}

		order.setStatus(OrderStatus.OUT_FOR_DELIVERY);
	}

	@Override
	@Transactional
	public void markDelivered(Long orderId) {

		Order order = orderRepository.findById(orderId)
				.orElseThrow(() -> new ResourceNotFoundException("Order not found"));

		if (order.getStatus() != OrderStatus.OUT_FOR_DELIVERY) {
			throw new BadRequestException("Order cannot be marked delivered from status: " + order.getStatus());
		}

		order.setStatus(OrderStatus.DELIVERED);
	}

	@Override
	@Transactional
	public void markCancelledByDelivery(Long orderId) {

		Order order = orderRepository.findById(orderId)
				.orElseThrow(() -> new ResourceNotFoundException("Order not found"));

		OrderStatus currentStatus = order.getStatus();

		if (currentStatus == OrderStatus.DELIVERED || currentStatus == OrderStatus.CANCELLED
				|| currentStatus == OrderStatus.PAYMENT_FAILED) {

			throw new BadRequestException("Order cannot be cancelled from status: " + currentStatus);
		}

		// Update order status
		order.setStatus(OrderStatus.CANCELLED);

		// Release inventory
		for (OrderItem item : order.getOrderItems()) {

			InventoryReleaseRequest request = new InventoryReleaseRequest();
			request.setQuantity(item.getQuantity());

			inventoryFeignClient.releaseStock(item.getProductId(), request);
		}
	}

}
