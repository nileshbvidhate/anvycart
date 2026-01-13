package com.nv.cart.service;

import com.nv.cart.dto.CartResponse;

public interface ProtectedCartService {

	CartResponse getCart(Long userId);

	CartResponse addItem(Long userId, Long productId, Integer quantity);

	CartResponse patchItemQuantity(Long userId, Long cartItemId, Integer quantity);
	
	CartResponse removeItem(Long userId, Long cartItemId);
	
	CartResponse clearCart(Long userId);
}
