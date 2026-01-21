package com.nv.order.controller;

import java.time.LocalDateTime;

import org.springframework.data.domain.Page;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.nv.order.dto.response.ApiResponse;
import com.nv.order.dto.response.OrderResponse;
import com.nv.order.entity.OrderStatus;
import com.nv.order.security.AuthorizationUtil;
import com.nv.order.service.AdminOrderService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/orders")
@RequiredArgsConstructor
@Validated
public class AdminOrderController {

	private final AdminOrderService adminOrderService;

	private final AuthorizationUtil authorizationUtil;

	@GetMapping("/all")
	public ResponseEntity<ApiResponse<Page<OrderResponse>>> getAllOrders(@RequestParam(required = false) String status,
			@RequestParam(required = false) @NotNull(message = "userId should not be null") Long userId,
			@RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fromDate,
			@RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime toDate,
			@RequestParam(defaultValue = "createdAt") String sortBy,
			@RequestParam(defaultValue = "desc") String direction, @RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "20") int size, HttpServletRequest httpServletRequest) {

		authorizationUtil.requireAdmin(httpServletRequest);

		OrderStatus oStatus = OrderStatus.valueOf(status.toUpperCase());

		Page<OrderResponse> response = adminOrderService.getAllOrders(oStatus, userId, fromDate, toDate, sortBy,
				direction, page, size);

		return ResponseEntity.ok(new ApiResponse<>(HttpStatus.OK.value(), "Orders fetched successfully", response));
	}

	@PatchMapping("/{orderId}/confirm")
	public ResponseEntity<ApiResponse<Void>> confirmOrder(
			@PathVariable @NotNull(message = "userId should not be null") Long orderId,
			HttpServletRequest httpServletRequest) {

		authorizationUtil.requireAdmin(httpServletRequest);

		adminOrderService.confirmOrder(orderId);

		return ResponseEntity
				.ok(new ApiResponse<>(HttpStatus.NO_CONTENT.value(), "Order confirmed successfully", null));
	}

}
