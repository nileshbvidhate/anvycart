package com.nv.cart.mapper;

import org.springframework.stereotype.Component;

import com.nv.cart.dto.CartItemResponse;
import com.nv.cart.entity.CartItem;

@Component
public class CartItemMapper {

	public CartItemResponse toCartItemResponse(CartItem cartItem) {
		
		return new CartItemResponse(cartItem.getId(), cartItem.getProductId(), cartItem.getQuantity());
	}
}
