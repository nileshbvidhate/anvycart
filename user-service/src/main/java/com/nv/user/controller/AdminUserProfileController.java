package com.nv.user.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.nv.user.dto.ApiResponse;
import com.nv.user.dto.UserProfileResponse;
import com.nv.user.security.AuthorizationUtil;
import com.nv.user.service.UserProfileService;

import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/users")
public class AdminUserProfileController {

	@Autowired
	private UserProfileService userProfileService;
	
	@Autowired AuthorizationUtil authorizationUtil;

	@GetMapping
	public ResponseEntity<ApiResponse<Page<UserProfileResponse>>> getAllUsers(
			@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size, HttpServletRequest request) {

		authorizationUtil.requireAdmin(request);
		
		Pageable pageable = PageRequest.of(page, size);

		Page<UserProfileResponse> response = userProfileService.getAllUsers(pageable);

		return ResponseEntity.ok(new ApiResponse<>(HttpStatus.OK.value(), "Users fetched successfully", response));
	}

	@GetMapping("/{userId}")
	public ResponseEntity<ApiResponse<UserProfileResponse>> getUserByUserId(@PathVariable Long userId, HttpServletRequest request) {

		authorizationUtil.requireAdmin(request);
		
		UserProfileResponse response = userProfileService.getUserByUserId(userId);

		return ResponseEntity.ok(new ApiResponse<>(HttpStatus.OK.value(), "User fetched successfully", response));
	}
}
