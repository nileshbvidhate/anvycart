package com.nv.order.service;

import java.time.LocalDateTime;

import org.springframework.data.domain.Page;

import com.nv.order.dto.response.OrderResponse;
import com.nv.order.entity.OrderStatus;

public interface AdminOrderService {

	void confirmOrder(Long orderId);

	Page<OrderResponse> getAllOrders(OrderStatus status, Long userId, LocalDateTime fromDate, LocalDateTime toDate,
			String sortBy, String direction, int page, int size);
}
