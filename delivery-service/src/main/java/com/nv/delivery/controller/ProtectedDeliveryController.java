package com.nv.delivery.controller;

import com.nv.delivery.dto.response.ApiResponse;
import com.nv.delivery.dto.response.DeliveryUserResponse;
import com.nv.delivery.service.ProtectedDeliveryService;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/deliveries")
@RequiredArgsConstructor
public class ProtectedDeliveryController {

	private final ProtectedDeliveryService protectedDeliveryService;

	@GetMapping("/order/{orderId}")
	public ResponseEntity<ApiResponse<DeliveryUserResponse>> getDeliveryByOrderId(
			@PathVariable @NotNull(message = "orderId should not be null") Long orderId,
			@RequestHeader("X-User-Id") @NotNull(message = "userId should not be null") Long userId) {

		DeliveryUserResponse response = protectedDeliveryService.getDeliveryByOrderId(orderId, userId);

		return ResponseEntity
				.ok(new ApiResponse<>(HttpStatus.OK.value(), "Delivery details fetched successfully", response));
	}
}
