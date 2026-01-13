package com.nv.cart.dto;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class CartResponse {
	
	private Long id;

	List<CartItemResponse> items;
	
}
