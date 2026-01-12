package com.nv.product.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StockResponse {
	private Integer totalQuantity;
	private Integer reservedQuantity;
	private Integer availableQuantity;
}
