package com.nv.auth.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import com.nv.auth.dto.AccountStatus;
import com.nv.auth.dto.PasswordReset;
import com.nv.auth.dto.RegisterRequest;
import com.nv.auth.dto.UserResponse;
import com.nv.auth.dto.UserRole;
import com.nv.auth.entity.AuthUser;

public interface AuthService {

	// public services
	public AuthUser getAuthUserByUsername(String name);

	public void register(RegisterRequest request);

	// protected services
	public void changePassword(String username, PasswordReset passwordReset);

	// Admin Services
	public Page<UserResponse> getAllUsers(Pageable pageable, AccountStatus status, UserRole role);

	UserResponse getUserById(Long userId);

	UserResponse updateUserStatus(Long userId, AccountStatus status);

	UserResponse updateUserRole(Long userId, UserRole role);
}
