package com.nv.auth.controller;

import com.nv.auth.dto.UserResponse;
import com.nv.auth.dto.AccountStatus;
import com.nv.auth.dto.ApiResponse;
import com.nv.auth.dto.UserRole;
import com.nv.auth.service.AuthService;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth/users")
@RequiredArgsConstructor
public class AdminController {

	private final AuthService authService;

	@GetMapping()
	@PreAuthorize("hasRole('ADMIN')") // Only allow ROLE_ADMIN users
	public ResponseEntity<ApiResponse<Page<UserResponse>>> getAllUsers(@RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "10") int size, @RequestParam(required = false) String status,
			@RequestParam(required = false) String role) {

		AccountStatus eStatus = (status != null) ? AccountStatus.valueOf(status.toUpperCase()) : null;

		UserRole eRole = (role != null) ? UserRole.valueOf(role.toUpperCase()) : null;

		Page<UserResponse> users = authService.getAllUsers(PageRequest.of(page, size), eStatus, eRole);

		return ResponseEntity.ok(new ApiResponse<>(HttpStatus.OK.value(), "Users fetched successfully", users));
	}

	@PreAuthorize("hasRole('ADMIN')")
	@GetMapping("/{userId}")
	public ResponseEntity<ApiResponse<UserResponse>> getUserById(@PathVariable Long userId) {

		UserResponse user = authService.getUserById(userId);

		return ResponseEntity.ok(new ApiResponse<>(HttpStatus.OK.value(), "User fetched successfully", user));
	}

	@PreAuthorize("hasRole('ADMIN')")
	@PutMapping("/{userId}/status")
	public ResponseEntity<ApiResponse<UserResponse>> updateStatus(@PathVariable Long userId,
			@RequestParam @NotBlank String status) {

		AccountStatus eStatus = AccountStatus.valueOf(status.toUpperCase());

		UserResponse updatedUser = authService.updateUserStatus(userId, eStatus);

		return ResponseEntity
				.ok(new ApiResponse<>(HttpStatus.OK.value(), "User status updated successfully", updatedUser));
	}

	@PreAuthorize("hasRole('ADMIN')")
	@PutMapping("/{userId}/role")
	public ResponseEntity<ApiResponse<UserResponse>> updateRole(@PathVariable Long userId,
			@RequestParam @NotBlank String role) {

		UserRole eRole = UserRole.valueOf(role.toUpperCase());
		
		UserResponse updatedUser = authService.updateUserRole(userId, eRole);

		return ResponseEntity
				.ok(new ApiResponse<>(HttpStatus.OK.value(), "User role updated successfully", updatedUser));
	}
}