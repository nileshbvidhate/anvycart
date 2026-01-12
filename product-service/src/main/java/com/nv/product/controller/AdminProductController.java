package com.nv.product.controller;

import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.nv.product.dto.ApiResponse;
import com.nv.product.dto.ProductCreateRequest;
import com.nv.product.dto.ProductPatchRequest;
import com.nv.product.dto.ProductResponse;
import com.nv.product.dto.ProductUpdateRequest;
import com.nv.product.security.AuthorizationUtil;
import com.nv.product.service.AdminProductService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/products")
@Validated
@RequiredArgsConstructor
public class AdminProductController {

	private final AdminProductService adminProductService;

	private final AuthorizationUtil authorizationUtil;

	@PostMapping
	public ResponseEntity<ApiResponse<ProductResponse>> createProduct(@RequestBody @Valid ProductCreateRequest request,
			HttpServletRequest httpRequest) {

		authorizationUtil.requireAdmin(httpRequest);

		ProductResponse response = adminProductService.createProduct(request);

		return ResponseEntity.status(HttpStatus.CREATED)
				.body(new ApiResponse<>(HttpStatus.CREATED.value(), "Product created successfully", response));
	}

	@PutMapping("/{productId}")
	public ResponseEntity<ApiResponse<ProductResponse>> updateProduct(
			@PathVariable @NotNull(message = "productId should not be null") Long productId,
			@RequestBody @Valid ProductUpdateRequest request, HttpServletRequest httpRequest) {

		authorizationUtil.requireAdmin(httpRequest);

		ProductResponse response = adminProductService.updateProduct(productId, request);

		return ResponseEntity.ok(new ApiResponse<>(HttpStatus.OK.value(), "Product updated successfully", response));
	}

	@PatchMapping("/{productId}")
	public ResponseEntity<ApiResponse<ProductResponse>> patchProduct(
			@PathVariable @NotNull(message = "productId should not be null") Long productId,
			@RequestBody ProductPatchRequest request, HttpServletRequest httpRequest) {

		authorizationUtil.requireAdmin(httpRequest);

		ProductResponse response = adminProductService.patchProduct(productId, request);

		return ResponseEntity.ok(new ApiResponse<>(HttpStatus.OK.value(), "Product updated successfully", response));
	}

	@GetMapping("/inactive")
	public ResponseEntity<ApiResponse<Page<ProductResponse>>> getInactiveProducts(
			@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size,
			@RequestParam(required = false) String sortBy, @RequestParam(defaultValue = "asc") String direction,
			HttpServletRequest httpRequest) {

		authorizationUtil.requireAdmin(httpRequest);

		Page<ProductResponse> products = adminProductService.getInactiveProducts(page, size, sortBy, direction);

		return ResponseEntity
				.ok(new ApiResponse<>(HttpStatus.OK.value(), "Inactive products fetched successfully", products));
	}

}
