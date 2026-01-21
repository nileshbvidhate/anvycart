package com.nv.delivery.controller;

import com.nv.delivery.dto.request.DeliveryCreateRequest;
import com.nv.delivery.dto.response.ApiResponse;
import com.nv.delivery.dto.response.InternalDeliveryResponse;
import com.nv.delivery.service.InternalDeliveryService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/internal/deliveries")
@RequiredArgsConstructor
public class InternalDeliveryController {

	private final InternalDeliveryService internalDeliveryService;

	@PostMapping
	public ResponseEntity<ApiResponse<Void>> createDelivery(@Valid @RequestBody DeliveryCreateRequest request) {

		internalDeliveryService.createDelivery(request);

		return ResponseEntity.status(HttpStatus.CREATED)
				.body(new ApiResponse<>(HttpStatus.CREATED.value(), "Delivery created successfully", null));
	}

	@GetMapping("/order/{orderId}")
	public ResponseEntity<ApiResponse<InternalDeliveryResponse>> getDeliveryByOrderId(
			@PathVariable @NotNull(message = "orderId can not be null") Long orderId) {

		InternalDeliveryResponse response = internalDeliveryService.getDeliveryByOrderId(orderId);

		return ResponseEntity.ok(new ApiResponse<>(HttpStatus.OK.value(), "Delivery fetched successfully", response));
	}

	@PatchMapping("/order/{orderId}/cancel")
	public ResponseEntity<ApiResponse<Void>> cancelDeliveryByOrderId(
			@PathVariable @NotNull(message = "orderId can not be null") Long orderId) {

		internalDeliveryService.cancelDeliveryByOrderId(orderId);

		return ResponseEntity
				.ok(new ApiResponse<>(HttpStatus.NO_CONTENT.value(), "Delivery cancelled successfully", null));
	}

}
