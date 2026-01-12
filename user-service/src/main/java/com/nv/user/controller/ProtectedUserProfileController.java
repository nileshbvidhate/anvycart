package com.nv.user.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.nv.user.dto.ApiResponse;
import com.nv.user.dto.PatchUserProfileRequest;
import com.nv.user.dto.UserProfileRequest;
import com.nv.user.dto.UserProfileResponse;
import com.nv.user.service.UserProfileService;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/users/me")
public class ProtectedUserProfileController {

	@Autowired
	private UserProfileService userProfileService;

	@GetMapping("/profile")
	public ResponseEntity<ApiResponse<UserProfileResponse>> getMyProfile(@RequestHeader("X-User-Id") Long userId) {

		UserProfileResponse userProfileResponse = userProfileService.getMyProfile(userId);

		return ResponseEntity
				.ok(new ApiResponse<>(HttpStatus.OK.value(), "Profile fetched successfully", userProfileResponse));
	}

	@PostMapping("/profile")
	public ResponseEntity<ApiResponse<UserProfileResponse>> createMyProfile(@RequestHeader("X-User-Id") Long userId,
			@Valid @RequestBody UserProfileRequest request) {

		UserProfileResponse userProfileResponse = userProfileService.createMyProfile(userId, request);

		return ResponseEntity.status(HttpStatus.CREATED).body(
				new ApiResponse<>(HttpStatus.CREATED.value(), "Profile created successfully", userProfileResponse));
	}

	@PutMapping("/profile")
	public ResponseEntity<ApiResponse<UserProfileResponse>> updateMyProfile(@RequestHeader("X-User-Id") Long userId,
			@Valid @RequestBody UserProfileRequest request) {

		UserProfileResponse response = userProfileService.updateMyProfile(userId, request);

		return ResponseEntity.ok(new ApiResponse<>(HttpStatus.OK.value(), "Profile updated successfully", response));
	}

	@PatchMapping("/profile")
	public ResponseEntity<ApiResponse<UserProfileResponse>> patchMyProfile(@RequestHeader("X-User-Id") Long userId,
			@RequestBody PatchUserProfileRequest request) {

		UserProfileResponse response = userProfileService.patchMyProfile(userId, request);

		return ResponseEntity.ok(new ApiResponse<>(HttpStatus.OK.value(), "Profile updated successfully", response));
	}

}
