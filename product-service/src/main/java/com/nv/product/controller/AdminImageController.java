package com.nv.product.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import com.nv.product.dto.ApiResponse;
import com.nv.product.dto.ImageResponse;
import com.nv.product.security.AuthorizationUtil;
import com.nv.product.service.AdminImageService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/products")
@Validated
@RequiredArgsConstructor
public class AdminImageController {

	private final AdminImageService adminImageService;

	private final AuthorizationUtil authorizationUtil;

	@PostMapping("/{productId}/images")
	public ResponseEntity<ApiResponse<ImageResponse>> uploadProductImage(
			@PathVariable @NotNull(message = "productId should not be null") Long productId,
			@RequestParam @NotNull(message = "Image file is required") MultipartFile file,
			@RequestParam(defaultValue = "false") boolean isPrimary, HttpServletRequest httpRequest) {

		authorizationUtil.requireAdmin(httpRequest);

		ImageResponse response = adminImageService.uploadProductImage(productId, file, isPrimary);

		return ResponseEntity.status(HttpStatus.CREATED)
				.body(new ApiResponse<>(HttpStatus.CREATED.value(), "Image uploaded successfully", response));
	}

	@DeleteMapping("/{productId}/images/{imageId}")
	public ResponseEntity<ApiResponse<Void>> deleteImage(
			@PathVariable @NotNull(message = "productId should not be null") Long productId,
			@PathVariable @NotNull(message = "imageId should not be null") Long imageId,
			HttpServletRequest httpRequest) {

		authorizationUtil.requireAdmin(httpRequest);

		adminImageService.deleteProductImage(productId, imageId);

		return ResponseEntity.ok(new ApiResponse<>(HttpStatus.NO_CONTENT.value(), "Image deleted successfully", null));
	}

	@PatchMapping("/{productId}/images/{imageId}")
	public ResponseEntity<ApiResponse<ImageResponse>> PatchImage(
			@PathVariable @NotNull(message = "productId should not be null") Long productId,
			@PathVariable @NotNull(message = "imageId should not be null") Long imageId,
			@RequestParam(required = false) MultipartFile file, @RequestParam(required = false) Boolean isPrimary,
			HttpServletRequest httpRequest) {

		authorizationUtil.requireAdmin(httpRequest);

		ImageResponse response = adminImageService.patchImage(productId, imageId, file, isPrimary);

		return ResponseEntity.ok(new ApiResponse<>(HttpStatus.OK.value(), "Image updated successfully", response));
	}

}
