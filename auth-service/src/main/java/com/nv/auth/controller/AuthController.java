package com.nv.auth.controller;

import com.nv.auth.dto.ApiResponse;
import com.nv.auth.dto.LoginRequest;
import com.nv.auth.dto.LoginResponse;
import com.nv.auth.dto.PasswordReset;
import com.nv.auth.dto.RegisterRequest;
import com.nv.auth.entity.AuthUser;
import com.nv.auth.jwt.JwtUtil;
import com.nv.auth.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

	@Autowired
	private AuthenticationManager authenticationManager;

	@Autowired
	private JwtUtil jwtUtil;

	@Autowired
	private AuthService authService;

	@PostMapping("/login")
	public ResponseEntity<ApiResponse<LoginResponse>> login(@Valid @RequestBody LoginRequest request) {

		UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
				request.getUsername(), request.getPassword());

		Authentication authentication = authenticationManager.authenticate(usernamePasswordAuthenticationToken);

		AuthUser authUser = authService.getAuthUserByUsername(authentication.getName());

		String token = jwtUtil.generateToken(authentication, authUser.getId());

		LoginResponse loginResponse = new LoginResponse();

		loginResponse.setToken(token);
		loginResponse.setTokenType("Bearer");

		return ResponseEntity.ok(new ApiResponse<>(HttpStatus.OK.value(), "Login Successful", loginResponse));
	}

	@PostMapping("/register")
	public ResponseEntity<ApiResponse<Void>> register(@Valid @RequestBody RegisterRequest request) {

		authService.register(request);

		ApiResponse<Void> response = new ApiResponse<>();

		response.setStatus(HttpStatus.CREATED.value());
		response.setMessage("User registered successfully.");
		response.setData(null);

		return ResponseEntity.status(HttpStatus.CREATED).body(response);
	}

	@PostMapping("/password/change")
	public ResponseEntity<ApiResponse<Void>> changePassword(@Valid @RequestBody PasswordReset passwordReset,   JwtAuthenticationToken authentication) {

		String username = authentication.getToken().getSubject();
		
		authService.changePassword(username, passwordReset);
		
		return ResponseEntity.status(HttpStatus.OK)
				.body(new ApiResponse<>(HttpStatus.OK.value(),  "Password changed successfully", null));
	}

}
