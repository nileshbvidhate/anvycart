package com.nv.inventory.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AdjustStockRequest {

	@NotNull(message = "Total Quantities are required to adjust the stock it can be positive or negative")
	private Integer totalQuantity;
}
