package com.nv.payment.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.nv.payment.dto.response.AdminPaymentResponse;
import com.nv.payment.dto.response.ApiResponse;
import com.nv.payment.security.AuthorizationUtil;
import com.nv.payment.service.AdminPaymentService;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/payments")
@RequiredArgsConstructor
public class AdminPaymentController {

	private final AdminPaymentService adminPaymentService;
	
	private final AuthorizationUtil authorizationUtil;

	@GetMapping
	public ResponseEntity<ApiResponse<Page<AdminPaymentResponse>>> getAllPayments(
			@RequestParam(required = false) String status, Pageable pageable, HttpServletRequest httpServletRequest) {
		
		authorizationUtil.requireAdmin(httpServletRequest);

		Page<AdminPaymentResponse> response = adminPaymentService.getAllPayments(status, pageable);

		return ResponseEntity
				.ok(new ApiResponse<>(HttpStatus.OK.value(), "Payments records Fetch Successfully", response));
	}
}
