package com.nv.payment.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.nv.payment.dto.response.ApiResponse;
import com.nv.payment.dto.response.UserPaymentResponse;
import com.nv.payment.service.ProtectedPaymentService;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/payments")
@RequiredArgsConstructor
public class ProtectedPaymentController {

	private final ProtectedPaymentService protectedPaymentService;

	@GetMapping("/order/{orderId}")
	public ResponseEntity<ApiResponse<List<UserPaymentResponse>>> getPaymentsByOrder(@PathVariable Long orderId) {

		List<UserPaymentResponse> response = protectedPaymentService.getPaymentsByOrderId(orderId);

		return ResponseEntity
				.ok(new ApiResponse<>(HttpStatus.OK.value(), "Payment details fetched successfully", response));
	}
}
