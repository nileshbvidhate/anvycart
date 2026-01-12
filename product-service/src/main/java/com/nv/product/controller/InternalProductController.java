package com.nv.product.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.nv.product.dto.ApiResponse;
import com.nv.product.dto.InternalProductResponse;
import com.nv.product.service.InternalProductService;

import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/internal/products")
@RequiredArgsConstructor
@Validated
public class InternalProductController {

	private final InternalProductService internalProductService;

	@GetMapping("/{productId}")
	public ResponseEntity<ApiResponse<InternalProductResponse>> getInternalProduct(
			@PathVariable @NotNull(message = "productId should not be null") Long productId) {

		InternalProductResponse product = internalProductService.getInternalProduct(productId);

		return ResponseEntity.ok(new ApiResponse<>(HttpStatus.OK.value(), "Product fetched successfully", product));
	}
}
