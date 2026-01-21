package com.nv.delivery.controller;

import com.nv.delivery.dto.request.DeliveryExpectedDateUpdateRequest;
import com.nv.delivery.dto.response.ApiResponse;
import com.nv.delivery.dto.response.DeliveryAdminResponse;
import com.nv.delivery.entity.DeliveryStatus;
import com.nv.delivery.security.AuthorizationUtil;
import com.nv.delivery.service.AdminDeliveryService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import java.time.LocalDateTime;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/deliveries")
@RequiredArgsConstructor
public class AdminDeliveryController {

	private final AdminDeliveryService adminDeliveryService;

	private final AuthorizationUtil authorizationUtil;

	@PatchMapping("/{deliveryId}/status")
	public ResponseEntity<ApiResponse<Void>> updateDeliveryStatus(
			@PathVariable @NotNull(message = "deliveryId should not be null") Long deliveryId,

			@RequestParam @NotNull(message = "deliveryStatus should not be null") String status,
			HttpServletRequest httpServletRequest) {

		authorizationUtil.requireAdmin(httpServletRequest);

		DeliveryStatus eStatus = DeliveryStatus.valueOf(status.toUpperCase());

		adminDeliveryService.updateDeliveryStatus(deliveryId, eStatus);

		return ResponseEntity
				.ok(new ApiResponse<>(HttpStatus.NO_CONTENT.value(), "Delivery status updated successfully", null));
	}

	@PatchMapping("/{deliveryId}/expected-date")
	public ResponseEntity<ApiResponse<Void>> updateExpectedDeliveryDate(
			@PathVariable @NotNull(message = "deliveryId should not be null") Long deliveryId,

			@Valid @RequestBody DeliveryExpectedDateUpdateRequest request, HttpServletRequest httpServletRequest) {

		authorizationUtil.requireAdmin(httpServletRequest);

		adminDeliveryService.updateExpectedDeliveryDate(deliveryId, request.getExpectedDeliveryAt());

		return ResponseEntity.ok(new ApiResponse<>(HttpStatus.NO_CONTENT.value(),
				"Expected delivery date and time updated successfully", null));
	}

	@GetMapping
	public ResponseEntity<ApiResponse<Page<DeliveryAdminResponse>>> getDeliveries(
			@RequestParam(required = false) String status, @RequestParam(required = false) Long orderId,
			@RequestParam(required = false) Long userId, @RequestParam(required = false) LocalDateTime fromDate,
			@RequestParam(required = false) LocalDateTime toDate, Pageable pageable,
			HttpServletRequest httpServletRequest) {

		authorizationUtil.requireAdmin(httpServletRequest);

		DeliveryStatus eStatus = DeliveryStatus.valueOf(status.toUpperCase());
		Page<DeliveryAdminResponse> deliveries = adminDeliveryService.getDeliveries(eStatus, orderId, userId, fromDate,
				toDate, pageable);

		return ResponseEntity
				.ok(new ApiResponse<>(HttpStatus.OK.value(), "Deliveries fetched successfully", deliveries));
	}
}
