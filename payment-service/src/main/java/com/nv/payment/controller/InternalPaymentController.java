package com.nv.payment.controller;

import com.nv.payment.dto.request.RazorpayOrderCreateRequest;
import com.nv.payment.dto.request.RazorpayPaymentVerifyRequest;
import com.nv.payment.dto.response.ApiResponse;
import com.nv.payment.dto.response.RazorpayOrderCreateResponse;
import com.nv.payment.dto.response.RazorpayPaymentVerifyResponse;
import com.nv.payment.service.InternalPaymentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/internal/payments")
@RequiredArgsConstructor
@Validated
public class InternalPaymentController {

	private final InternalPaymentService paymentService;

	@PostMapping
	public ApiResponse<RazorpayOrderCreateResponse> createPayment(
			@Valid @RequestBody RazorpayOrderCreateRequest request) {

		RazorpayOrderCreateResponse response = paymentService.createOrder(request);

		return new ApiResponse<>(HttpStatus.CREATED.value(), "Payment order created successfully", response);
	}

	@PostMapping("/verify-payment")
	public ApiResponse<RazorpayPaymentVerifyResponse> verifyPayment(
			@Valid @RequestBody RazorpayPaymentVerifyRequest request) {

		RazorpayPaymentVerifyResponse response = paymentService.verifyPayment(request);

		return new ApiResponse<>(HttpStatus.OK.value(), "Payment verification processed", response);
	}

}
