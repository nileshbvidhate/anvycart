package com.nv.order.service;

import java.util.List;

import com.nv.order.dto.request.BuyNowOrderRequest;
import com.nv.order.dto.request.OrderCreateRequest;
import com.nv.order.dto.request.OrderPaymentVerifyRequest;
import com.nv.order.dto.response.CreateOrderResponse;
import com.nv.order.dto.response.OrderPaymentVerifyResponse;
import com.nv.order.dto.response.OrderResponse;

public interface ProtectedOrderService {

	CreateOrderResponse createOrder(Long userId, OrderCreateRequest request);

	CreateOrderResponse buyNow(Long userId, BuyNowOrderRequest request);

	OrderResponse getOrderById(Long userId, Long orderId);

	List<OrderResponse> getOrders(Long userId);

	void cancelOrder(Long userId, Long orderId);

	CreateOrderResponse retryPayment(Long orderId);

	OrderPaymentVerifyResponse verifyPayment(Long orderId, OrderPaymentVerifyRequest request);

}
