package com.nv.order.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.nv.order.dto.request.OrderPaymentVerifyRequest;
import com.nv.order.dto.request.RazorpayOrderCreateRequest;
import com.nv.order.dto.response.ApiResponse;
import com.nv.order.dto.response.RazorpayOrderCreateResponse;
import com.nv.order.dto.response.RazorpayPaymentVerifyResponse;

@FeignClient(name = "payment-service", url = "${services.payment-service.url}")
public interface PaymentFeignClient {

	@PostMapping
	ApiResponse<RazorpayOrderCreateResponse> createRazorpayOrder(@RequestBody RazorpayOrderCreateRequest request);
	
	@PostMapping("/verify-payment")
	ApiResponse<RazorpayPaymentVerifyResponse> verifyPayment(@RequestBody OrderPaymentVerifyRequest request);
}