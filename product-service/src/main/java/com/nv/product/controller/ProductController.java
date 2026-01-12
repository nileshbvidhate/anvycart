package com.nv.product.controller;

import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.nv.product.dto.ApiResponse;
import com.nv.product.dto.ProductResponse;
import com.nv.product.service.ProductService;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/public/products")
@RequiredArgsConstructor
@Validated
public class ProductController {

	private final ProductService productService;

	@GetMapping
	public ResponseEntity<ApiResponse<Page<ProductResponse>>> getAllProducts(@RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "10") int size, @RequestParam(required = false) String sortBy,
			@RequestParam(defaultValue = "asc") String direction) {

		Page<ProductResponse> products = productService.getAllProducts(page, size, sortBy, direction);

		return ResponseEntity.ok(new ApiResponse<>(HttpStatus.OK.value(), "Products fetched successfully", products));
	}

	@GetMapping("/{productId}")
	public ResponseEntity<ApiResponse<ProductResponse>> getProductById(
			@PathVariable @NotNull(message = "productId should not be null") Long productId) {

		ProductResponse product = productService.getProductById(productId);

		return ResponseEntity.ok(new ApiResponse<>(HttpStatus.OK.value(), "Product fetched successfully", product));
	}

	@GetMapping(params = "categoryId")
	public ResponseEntity<ApiResponse<Page<ProductResponse>>> getProductsByCategory(
			@RequestParam @NotNull(message = "categoryId should not be null") Long categoryId,
			@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size,
			@RequestParam(required = false) String sortBy, @RequestParam(defaultValue = "asc") String direction) {

		Page<ProductResponse> products = productService.getProductsByCategory(categoryId, page, size, sortBy,
				direction);

		return ResponseEntity.ok(new ApiResponse<>(HttpStatus.OK.value(), "Products fetched successfully", products));
	}

	@GetMapping(params = "brand")
	public ResponseEntity<ApiResponse<Page<ProductResponse>>> getProductsByBrand(
			@RequestParam @NotBlank(message = "Brand name should not be null") String brand,
			@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size,
			@RequestParam(required = false) String sortBy, @RequestParam(defaultValue = "asc") String direction) {

		Page<ProductResponse> products = productService.getProductsByBrand(brand, page, size, sortBy, direction);

		return ResponseEntity.ok(new ApiResponse<>(HttpStatus.OK.value(), "Products fetched successfully", products));
	}

	@GetMapping(params = "search")
	public ResponseEntity<ApiResponse<Page<ProductResponse>>> searchProducts(
			@RequestParam @NotBlank(message = "Search keyword should not be null") String search,
			@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size,
			@RequestParam(required = false) String sortBy, @RequestParam(defaultValue = "asc") String direction) {

		Page<ProductResponse> products = productService.searchProducts(search, page, size, sortBy, direction);

		return ResponseEntity.ok(new ApiResponse<>(HttpStatus.OK.value(), "Products fetched successfully", products));
	}

}
