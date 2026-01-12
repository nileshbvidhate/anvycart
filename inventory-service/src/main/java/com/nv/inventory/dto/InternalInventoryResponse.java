package com.nv.inventory.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class InternalInventoryResponse {
	
	private Long id;
	private Long productId;
	private Integer totalQuantity;
	private Integer reservedQuantity;
	private Integer availableQuantity;
	
}
