package com.nv.inventory.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class InventoryCreateRequest {

	@NotNull(message = "Total quantity is required")
	@Min(value = 0, message = "Quantity must be at least 1")
	private Integer totalQuantity;
}
