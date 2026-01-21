package com.nv.order.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import com.nv.order.dto.response.ApiResponse;
import com.nv.order.dto.response.InternalOrderResponse;
import com.nv.order.service.InternalOrderService;

import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/internal/orders")
@RequiredArgsConstructor
@Validated
public class InternalOrderController {

	private final InternalOrderService internalOrderService;

	@PatchMapping("/{orderId}/payment-success")
	public ResponseEntity<ApiResponse<Void>> paymentSuccess(
			@PathVariable @NotNull(message = "orderId should not be null") Long orderId) {

		internalOrderService.handlePaymentSuccess(orderId);

		return ResponseEntity
				.ok(new ApiResponse<>(HttpStatus.NO_CONTENT.value(), "Order payment marked as successful", null));
	}

	@PatchMapping("/{orderId}/payment-failed")
	public ResponseEntity<ApiResponse<Void>> paymentFailed(
			@PathVariable @NotNull(message = "orderId should not be null") Long orderId) {

		internalOrderService.handlePaymentFailed(orderId);

		return ResponseEntity
				.ok(new ApiResponse<>(HttpStatus.NO_CONTENT.value(), "Order payment marked as failed", null));
	}

	@GetMapping("/{orderId}")
	public ResponseEntity<ApiResponse<InternalOrderResponse>> getOrderById(
			@PathVariable @NotNull(message = "orderId should not be null") Long orderId) {

		InternalOrderResponse response = internalOrderService.getOrderById(orderId);

		return ResponseEntity
				.ok(new ApiResponse<>(HttpStatus.OK.value(), "Internal order fetched successfully", response));
	}

	@PatchMapping("/{orderId}/shipped")
	public ResponseEntity<ApiResponse<Void>> markOrderShipped(
			@PathVariable @NotNull(message = "orderId should not be null") Long orderId) {

		internalOrderService.markOrderShipped(orderId);

		return ResponseEntity.ok(new ApiResponse<>(HttpStatus.NO_CONTENT.value(), "Order marked as shipped", null));
	}

	@PatchMapping("/{orderId}/out-for-delivery")
	public ResponseEntity<ApiResponse<Void>> markOutForDelivery(
			@PathVariable @NotNull(message = "orderId should not be null") Long orderId) {

		internalOrderService.markOutForDelivery(orderId);

		return ResponseEntity
				.ok(new ApiResponse<>(HttpStatus.NO_CONTENT.value(), "Order marked as out for delivery", null));
	}

	@PatchMapping("/{orderId}/delivered")
	public ResponseEntity<ApiResponse<Void>> markDelivered(
			@PathVariable @NotNull(message = "orderId should not be null") Long orderId) {

		internalOrderService.markDelivered(orderId);

		return ResponseEntity.ok(new ApiResponse<>(HttpStatus.OK.value(), "Order marked as delivered", null));
	}

	@PatchMapping("/{orderId}/cancelled")
	public ResponseEntity<ApiResponse<Void>> markOrderCancelledByDelivery(
			@PathVariable @NotNull(message = "orderId should not be null") Long orderId) {

		internalOrderService.markCancelledByDelivery(orderId);

		return ResponseEntity
				.ok(new ApiResponse<>(HttpStatus.OK.value(), "Order marked as cancelled by delivery service", null));
	}

}
