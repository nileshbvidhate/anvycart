package com.nv.auth.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.nv.auth.dto.ApiResponse;
import com.nv.auth.dto.InternalUserResponse;
import com.nv.auth.security.AuthorizationUtil;
import com.nv.auth.service.InternalService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.constraints.NotBlank;

@RestController
@RequestMapping("/internal/auth/users")
public class InternalController {

	@Autowired
	private InternalService internalService;
	
	@Autowired
	private AuthorizationUtil authorizationUtil;
	
	
	@GetMapping("/by-email")
	public ResponseEntity<ApiResponse<InternalUserResponse>> getUserByEmail(@RequestParam @NotBlank String email, HttpServletRequest request) {

		authorizationUtil.requireGateway(request);
		
		InternalUserResponse user = internalService.getUserByEmail(email);

		return ResponseEntity.ok(new ApiResponse<>(HttpStatus.OK.value(), "User fetched successfully", user));
	}
}
