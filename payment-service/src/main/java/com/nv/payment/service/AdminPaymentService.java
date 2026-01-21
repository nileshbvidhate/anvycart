package com.nv.payment.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.nv.payment.dto.response.AdminPaymentResponse;

public interface AdminPaymentService {
	
	public Page<AdminPaymentResponse> getAllPayments(String status, Pageable pageable);
}
