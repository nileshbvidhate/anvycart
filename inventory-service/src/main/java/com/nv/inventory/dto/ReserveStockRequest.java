package com.nv.inventory.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReserveStockRequest {

	@NotNull(message = "Quntities are required to reserve")
	@Min(value = 1, message = "Quantity must be at least 1")
	private Integer quantity;
}
