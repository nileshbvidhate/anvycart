package com.nv.cart.mapper;

import java.util.Collections;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.nv.cart.dto.CartResponse;
import com.nv.cart.entity.Cart;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class CartMapper {

	private final CartItemMapper cartItemMapper;
	
	public CartResponse toCartResponse(Cart cart) {

		if (cart == null) {
			return new CartResponse(null, Collections.emptyList());
		}

		return new CartResponse(cart.getId(),
				cart.getItems().stream().map(cartItemMapper::toCartItemResponse).collect(Collectors.toList()));
	}
}
