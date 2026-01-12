package com.nv.product.controller;

import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.nv.product.dto.CategoryResponse;
import com.nv.product.dto.ApiResponse;
import com.nv.product.service.CategoryService;

import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/public/products/categories")
@RequiredArgsConstructor
@Validated
public class CategoryController {

	private final CategoryService categoryService;

	@GetMapping
	public ResponseEntity<ApiResponse<List<CategoryResponse>>> getAllCategories() {

		List<CategoryResponse> categories = categoryService.getAllCategories();

		return ResponseEntity
				.ok(new ApiResponse<>(HttpStatus.OK.value(), "Categories fetched successfully", categories));
	}

	@GetMapping("/{categoryId}")
	public ResponseEntity<ApiResponse<CategoryResponse>> getCategoryById(
			@PathVariable @NotNull(message = "CategoryId should not be null") Long categoryId) {

		CategoryResponse category = categoryService.getCategoryById(categoryId);

		return ResponseEntity.ok(new ApiResponse<>(HttpStatus.OK.value(), "Category fetched successfully", category));
	}
}
