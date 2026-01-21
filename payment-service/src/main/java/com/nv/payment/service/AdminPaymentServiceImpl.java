package com.nv.payment.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import com.nv.payment.dto.response.AdminPaymentResponse;
import com.nv.payment.entity.Payment;
import com.nv.payment.entity.PaymentStatus;
import com.nv.payment.mapper.PaymentMapper;
import com.nv.payment.repository.PaymentRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AdminPaymentServiceImpl implements AdminPaymentService {
	private final PaymentRepository paymentRepository;
	private final PaymentMapper paymentMapper;

	public Page<AdminPaymentResponse> getAllPayments(String status, Pageable pageable) {

		Page<Payment> payments;

		// Dynamic filter
		if (status != null && !status.isBlank()) {
			PaymentStatus paymentStatus = PaymentStatus.valueOf(status.toUpperCase());

			payments = paymentRepository.findByStatus(paymentStatus, pageable);
		} else {
			payments = paymentRepository.findAll(pageable);
		}

		return payments.map(paymentMapper::toAdminPaymentResponse);
	}
}
