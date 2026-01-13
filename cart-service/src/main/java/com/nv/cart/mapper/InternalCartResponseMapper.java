package com.nv.cart.mapper;

import java.util.Collections;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.nv.cart.dto.InternalCartItems;
import com.nv.cart.dto.InternalCartResponse;
import com.nv.cart.entity.Cart;

@Component
public class InternalCartResponseMapper {

	public InternalCartResponse toInternalCartResponse(Cart cart) {
		if (cart == null) {
			return new InternalCartResponse(Collections.emptyList());
		}

		return new InternalCartResponse(
				cart.getItems().stream().map(item -> new InternalCartItems(item.getProductId(), item.getQuantity()))
						.collect(Collectors.toList()));
	}
}
