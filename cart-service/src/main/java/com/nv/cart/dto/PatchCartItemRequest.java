package com.nv.cart.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class PatchCartItemRequest {
	
	@NotNull(message = "quantity should not be null")
    @Min(value = 1, message = "quantity must be at least 1")
    private Integer quantity;
}
