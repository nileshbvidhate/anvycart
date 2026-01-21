package com.nv.order.controller;

import com.nv.order.dto.request.BuyNowOrderRequest;
import com.nv.order.dto.request.OrderCreateRequest;
import com.nv.order.dto.request.OrderPaymentVerifyRequest;
import com.nv.order.dto.response.ApiResponse;
import com.nv.order.dto.response.CreateOrderResponse;
import com.nv.order.dto.response.OrderPaymentVerifyResponse;
import com.nv.order.dto.response.OrderResponse;
import com.nv.order.service.ProtectedOrderService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/orders")
@Validated
public class ProtectedOrderController {

	private final ProtectedOrderService protectedOrderService;

	public ProtectedOrderController(ProtectedOrderService protectedOrderService) {
		this.protectedOrderService = protectedOrderService;
	}

	@PostMapping
	public ResponseEntity<ApiResponse<CreateOrderResponse>> createOrder(@Valid @RequestBody OrderCreateRequest request,
			@RequestHeader("X-User-Id") @NotNull(message = "userId should not be null") Long userId) {

		CreateOrderResponse response = protectedOrderService.createOrder(userId, request);

		return ResponseEntity.status(HttpStatus.CREATED)
				.body(new ApiResponse<>(HttpStatus.CREATED.value(), "Order created successfully", response));
	}

	@PostMapping("/{orderId}/retry-payment")
	public ApiResponse<CreateOrderResponse> retryPayment(
			@PathVariable @NotNull(message = "orderId should not be null") Long orderId) {

		CreateOrderResponse response = protectedOrderService.retryPayment(orderId);

		return new ApiResponse<>(HttpStatus.CREATED.value(), "Payment retry initiated successfully", response);
	}

	@PostMapping("/buy-now")
	public ResponseEntity<ApiResponse<CreateOrderResponse>> buyNow(@Valid @RequestBody BuyNowOrderRequest request,
			@RequestHeader("X-User-Id") @NotNull(message = "userId should not be null") Long userId) {

		CreateOrderResponse response = protectedOrderService.buyNow(userId, request);

		return ResponseEntity.status(HttpStatus.CREATED)
				.body(new ApiResponse<>(HttpStatus.CREATED.value(), "Order created successfully", response));
	}

	@GetMapping("/{orderId}")
	public ResponseEntity<ApiResponse<OrderResponse>> getOrderById(@PathVariable Long orderId,
			@RequestHeader("X-User-Id") @NotNull(message = "userId should not be null") Long userId) {

		OrderResponse response = protectedOrderService.getOrderById(userId, orderId);

		return ResponseEntity.ok(new ApiResponse<>(HttpStatus.OK.value(), "Order fetched successfully", response));
	}

	@GetMapping
	public ResponseEntity<ApiResponse<List<OrderResponse>>> getOrders(
			@RequestHeader("X-User-Id") @NotNull(message = "userId should not be null") Long userId) {

		List<OrderResponse> response = protectedOrderService.getOrders(userId);

		return ResponseEntity.ok(new ApiResponse<>(HttpStatus.OK.value(), "Orders fetched successfully", response));
	}

	@PatchMapping("/{orderId}/cancel")
	public ResponseEntity<ApiResponse<Void>> cancelOrder(
			@PathVariable @NotNull(message = "orderId should not be null") Long orderId,
			@RequestHeader("X-User-Id") @NotNull(message = "userId should not be null") Long userId) {

		protectedOrderService.cancelOrder(userId, orderId);

		return ResponseEntity
				.ok(new ApiResponse<>(HttpStatus.NO_CONTENT.value(), "Order cancelled successfully", null));
	}

	@PostMapping("/{orderId}/verify-payment")
	public ApiResponse<OrderPaymentVerifyResponse> verifyPayment(
			@PathVariable @NotNull(message = "orderId should not be null") Long orderId,
			@Valid @RequestBody OrderPaymentVerifyRequest request) {

		OrderPaymentVerifyResponse response = protectedOrderService.verifyPayment(orderId, request);

		return new ApiResponse<>(HttpStatus.OK.value(), "Payment verification processed", response);
	}

}
