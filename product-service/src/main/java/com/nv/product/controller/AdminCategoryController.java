package com.nv.product.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.nv.product.dto.ApiResponse;
import com.nv.product.dto.CategoryCreateRequest;
import com.nv.product.dto.CategoryResponse;
import com.nv.product.security.AuthorizationUtil;
import com.nv.product.service.AdminCategoryService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/products/categories")
@Validated
@RequiredArgsConstructor
public class AdminCategoryController {

	private final AdminCategoryService adminCategoryService;

	private final AuthorizationUtil authorizationUtil;

	@PostMapping
	public ResponseEntity<ApiResponse<CategoryResponse>> createCategory(
			@RequestBody @Valid CategoryCreateRequest request, HttpServletRequest httpRequest) {

		authorizationUtil.requireAdmin(httpRequest);

		CategoryResponse response = adminCategoryService.createCategory(request);

		return ResponseEntity.status(HttpStatus.CREATED)
				.body(new ApiResponse<>(HttpStatus.CREATED.value(), "Category created successfully", response));
	}

	// PATCH CATEGORY
	@PatchMapping("/{categoryId}")
	public ResponseEntity<ApiResponse<CategoryResponse>> patchCategory(
			@PathVariable @NotNull(message = "categoryId should not be null") Long categoryId,
			@RequestBody CategoryCreateRequest request, HttpServletRequest httpRequest) {

		authorizationUtil.requireAdmin(httpRequest);

		CategoryResponse response = adminCategoryService.patchCategory(categoryId, request);

		return ResponseEntity.ok(new ApiResponse<>(200, "Category updated successfully", response));
	}

	@DeleteMapping("/{categoryId}")
	public ResponseEntity<ApiResponse<Void>> deleteCategory(
			@PathVariable @NotNull(message = "categoryId should not be null") Long categoryId,
			HttpServletRequest httpRequest) {

		authorizationUtil.requireAdmin(httpRequest);

		adminCategoryService.deleteCategory(categoryId);

		return ResponseEntity
				.ok(new ApiResponse<>(HttpStatus.NO_CONTENT.value(), "Category deleted successfully", null));
	}

}
