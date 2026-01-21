package com.nv.payment.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.nv.payment.dto.response.UserPaymentResponse;
import com.nv.payment.entity.Payment;
import com.nv.payment.exception.ResourceNotFoundException;
import com.nv.payment.mapper.PaymentMapper;
import com.nv.payment.repository.PaymentRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProtectedPaymentServiceImpl implements ProtectedPaymentService {

	private final PaymentRepository paymentRepository;
	private final PaymentMapper paymentMapper;

	public List<UserPaymentResponse> getPaymentsByOrderId(Long orderId) {

		List<Payment> payments = paymentRepository.findByOrderIdOrderByCreatedAtDesc(orderId);

		if (payments.isEmpty()) {
			throw new ResourceNotFoundException("No payments found for this order");
		}

		return payments.stream().map(paymentMapper::toUserPaymentResponse).collect(Collectors.toList());
	}
}
