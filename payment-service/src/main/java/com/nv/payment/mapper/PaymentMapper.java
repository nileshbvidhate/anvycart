package com.nv.payment.mapper;

import org.springframework.stereotype.Component;

import com.nv.payment.dto.response.AdminPaymentResponse;
import com.nv.payment.dto.response.RazorpayOrderCreateResponse;
import com.nv.payment.dto.response.UserPaymentResponse;
import com.nv.payment.entity.Payment;

@Component
public class PaymentMapper {

	public RazorpayOrderCreateResponse toRazorpayOrderCreateResponse(Payment payment) {
	
		return RazorpayOrderCreateResponse.builder()
				.razorpayOrderId(payment.getRazorpayOrderId())
				.amount(payment.getAmount())
				.currency(payment.getCurrency())
				.build();
	}
	
	public UserPaymentResponse toUserPaymentResponse(Payment payment) {

        UserPaymentResponse response = new UserPaymentResponse();
        response.setOrderId(payment.getOrderId());
        response.setAmount(payment.getAmount());
        response.setCurrency(payment.getCurrency());
        response.setStatus(payment.getStatus().name());
        response.setPaymentMethod(payment.getPaymentMethod());
        response.setCreatedAt(payment.getCreatedAt());

        return response;
    }
	
	public AdminPaymentResponse toAdminPaymentResponse(Payment payment) {

        AdminPaymentResponse response = new AdminPaymentResponse();
        response.setId(payment.getId());
        response.setOrderId(payment.getOrderId());
        response.setAmount(payment.getAmount());
        response.setCurrency(payment.getCurrency());
        response.setStatus(payment.getStatus().name());
        response.setPaymentMethod(payment.getPaymentMethod());
        response.setRazorpayOrderId(payment.getRazorpayOrderId());
        response.setRazorpayPaymentId(payment.getRazorpayPaymentId());
        response.setCreatedAt(payment.getCreatedAt());

        return response;
    }
}
