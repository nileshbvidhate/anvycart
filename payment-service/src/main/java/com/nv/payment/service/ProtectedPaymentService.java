package com.nv.payment.service;

import java.util.List;
import com.nv.payment.dto.response.UserPaymentResponse;

public interface ProtectedPaymentService {
	
	public List<UserPaymentResponse> getPaymentsByOrderId(Long orderId);
	
}
