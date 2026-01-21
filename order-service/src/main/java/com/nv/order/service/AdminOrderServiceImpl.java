package com.nv.order.service;

import java.time.LocalDateTime;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import com.nv.order.client.DeliveryFeignClient;
import com.nv.order.dto.request.DeliveryCreateRequest;
import com.nv.order.dto.response.OrderResponse;
import com.nv.order.entity.Order;
import com.nv.order.entity.OrderStatus;
import com.nv.order.exception.BadRequestException;
import com.nv.order.exception.ResourceNotFoundException;
import com.nv.order.mapper.OrderMapper;
import com.nv.order.repository.OrderRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AdminOrderServiceImpl implements AdminOrderService {

	private final OrderMapper orderMapper;

	private final OrderRepository orderRepository;
	private final DeliveryFeignClient deliveryFeignClient;

	@Override
	@Transactional
	public Page<OrderResponse> getAllOrders(OrderStatus status, Long userId, LocalDateTime fromDate,
			LocalDateTime toDate, String sortBy, String direction, int page, int size) {

		Sort sort = direction.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();

		Pageable pageable = PageRequest.of(page, size, sort);

		Specification<Order> spec = SpecificationBuilder.withFilters(status, userId, fromDate, toDate);

		return orderRepository.findAll(spec, pageable).map(orderMapper::toOrderResponse);
	}

	@Transactional
	@Override
	public void confirmOrder(Long orderId) {

		Order order = orderRepository.findById(orderId)
				.orElseThrow(() -> new ResourceNotFoundException("Order not found"));

		// Only PLACED orders can be confirmed
		if (order.getStatus() != OrderStatus.PLACED) {
			throw new BadRequestException("Order cannot be confirmed in current status: " + order.getStatus());
		}

		// STEP 1: Update order status
		order.setStatus(OrderStatus.CONFIRMED);

		// STEP 2: Create delivery

		DeliveryCreateRequest deliveryCreateRequest = new DeliveryCreateRequest();

		deliveryCreateRequest.setOrderId(order.getId());
		deliveryCreateRequest.setUserId(order.getUserId());
		deliveryCreateRequest.setAddressId(order.getAddressId());

		deliveryFeignClient.createDelivery(deliveryCreateRequest);
	}

}
