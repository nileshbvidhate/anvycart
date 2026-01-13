package com.nv.cart.service;

import com.nv.cart.dto.InternalCartResponse;

public interface InternalCartService {
	
	InternalCartResponse getCartForOrder(Long userId);
	
	void clearCartAfterOrder(Long userId);
}
