package com.nv.payment.service;

import com.nv.payment.dto.request.RazorpayOrderCreateRequest;
import com.nv.payment.dto.request.RazorpayPaymentVerifyRequest;
import com.nv.payment.dto.response.RazorpayOrderCreateResponse;
import com.nv.payment.dto.response.RazorpayPaymentVerifyResponse;

public interface InternalPaymentService {
	
	RazorpayOrderCreateResponse createOrder(RazorpayOrderCreateRequest request);
	
	public RazorpayPaymentVerifyResponse verifyPayment(RazorpayPaymentVerifyRequest request);
}
