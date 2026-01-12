package com.nv.inventory.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
public class ReleaseStockRequest {

	@NotNull(message="Qunatities are required")
	@Min(value = 1, message = "Quantity must be at least 1")
    private Integer quantity;
}

