package com.nv.cart.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.nv.cart.dto.ApiResponse;
import com.nv.cart.dto.InternalCartResponse;
import com.nv.cart.service.InternalCartService;

import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/internal/cart")
public class InternalCartController {

	private final InternalCartService internalCartService;

	@GetMapping("/{userId}")
	public ResponseEntity<ApiResponse<InternalCartResponse>> getCartForOrder(
			@PathVariable @NotNull(message = "userId should not be null") Long userId) {

		InternalCartResponse internalCartResponse = internalCartService.getCartForOrder(userId);

		return ResponseEntity
				.ok(new ApiResponse<>(HttpStatus.OK.value(), "Cart Items fetched successfully", internalCartResponse));

	}

	@DeleteMapping("/{userId}")
	public ResponseEntity<ApiResponse<Void>> clearCartAfterOrder(
			@PathVariable @NotNull(message = "userId should not be null") Long userId) {

		internalCartService.clearCartAfterOrder(userId);

		return ResponseEntity
				.ok(new ApiResponse<>(HttpStatus.OK.value(), "Cart cleared successfully after order", null));
	}
}
