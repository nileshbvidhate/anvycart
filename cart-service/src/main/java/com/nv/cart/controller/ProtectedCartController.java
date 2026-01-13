package com.nv.cart.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.nv.cart.dto.AddCartItemRequest;
import com.nv.cart.dto.ApiResponse;
import com.nv.cart.dto.CartResponse;
import com.nv.cart.dto.PatchCartItemRequest;
import com.nv.cart.service.ProtectedCartService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;

@RestController
@Validated
@RequiredArgsConstructor
@RequestMapping("/cart")
public class ProtectedCartController {

	private final ProtectedCartService protectedCartService;

	@GetMapping
	public ResponseEntity<ApiResponse<CartResponse>> getCart(
			@RequestHeader("X-User-Id") @NotNull(message = "userId should not be null") Long userId) {

		CartResponse cart = protectedCartService.getCart(userId);

		return ResponseEntity.status(HttpStatus.OK)
				.body(new ApiResponse<>(HttpStatus.OK.value(), "Cart fetched successfully", cart));
	}

	@PostMapping("/items")
	public ResponseEntity<ApiResponse<CartResponse>> addItem(
			@RequestHeader("X-User-Id") @NotNull @NotNull(message = "userId should not be null") Long userId,
			@Valid @RequestBody AddCartItemRequest request) {

		CartResponse cart = protectedCartService.addItem(userId, request.getProductId(), request.getQuantity());

		return ResponseEntity.status(HttpStatus.OK)
				.body(new ApiResponse<>(HttpStatus.OK.value(), "Item added to cart successfully", cart));
	}

	@PatchMapping("/items/{cartItemId}")
	public ResponseEntity<ApiResponse<CartResponse>> updateItemQuantity(
			@RequestHeader("X-User-Id") @NotNull @NotNull(message = "userId should not be null") Long userId,
			@PathVariable @NotNull(message = "cartItemId should not be null") Long cartItemId,
			@Valid @RequestBody PatchCartItemRequest request) {
		CartResponse cart = protectedCartService.patchItemQuantity(userId, cartItemId, request.getQuantity());

		return ResponseEntity
				.ok(new ApiResponse<>(HttpStatus.OK.value(), "Cart item quantity updated successfully", cart));
	}

	@DeleteMapping("/items/{cartItemId}")
	public ResponseEntity<ApiResponse<CartResponse>> removeItem(
			@RequestHeader("X-User-Id") @NotNull @NotNull(message = "userId should not be null") Long userId,
			@PathVariable @NotNull(message = "cartItemId should not be null") Long cartItemId) {
		CartResponse cart = protectedCartService.removeItem(userId, cartItemId);

		return ResponseEntity.ok(new ApiResponse<>(HttpStatus.OK.value(), "Cart item removed successfully", cart));
	}

	@DeleteMapping
	public ResponseEntity<ApiResponse<CartResponse>> clearCart(
			@RequestHeader("X-User-Id") @NotNull @NotNull(message = "userId should not be null") Long userId) {
		
		CartResponse cart = protectedCartService.clearCart(userId);

		return ResponseEntity.ok(new ApiResponse<>(HttpStatus.OK.value(), "Cart cleared successfully", cart));
	}

}
