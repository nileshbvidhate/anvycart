package com.nv.cart.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AddCartItemRequest {
	@NotNull(message = "productId should not be null")
    private Long productId;

    @NotNull(message = "quantity should not be null")
    @Min(value = 1, message = "quantity must be at least 1")
    private Integer quantity;
}
