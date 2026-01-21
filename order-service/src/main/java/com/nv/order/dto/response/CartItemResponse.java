package com.nv.order.dto.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CartItemResponse {

    private Long productId;
    private Integer quantity;
}