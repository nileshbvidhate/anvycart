package com.nv.user.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.nv.user.dto.ApiResponse;
import com.nv.user.dto.InternalUserProfileResponse;
import com.nv.user.service.UserProfileService;

@RestController
@RequestMapping("/internal/users")
public class InternalUserProfileController {
	
	@Autowired
	private UserProfileService userProfileService;

	@GetMapping("/{userId}")
	public ResponseEntity<ApiResponse<InternalUserProfileResponse>> getInternalUserProfile(@PathVariable Long userId) {

		InternalUserProfileResponse response = userProfileService.getInternalUserProfile(userId);

		return ResponseEntity
				.ok(new ApiResponse<>(HttpStatus.OK.value(), "Internal user fetched successfully", response));
	}
}
