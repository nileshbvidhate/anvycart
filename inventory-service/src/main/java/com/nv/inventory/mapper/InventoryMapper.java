package com.nv.inventory.mapper;

import org.springframework.stereotype.Component;

import com.nv.inventory.dto.InternalInventoryResponse;
import com.nv.inventory.entity.Inventory;

@Component
public class InventoryMapper {
	
	public InternalInventoryResponse toDto(Inventory inventory) {
		
		InternalInventoryResponse dto = new InternalInventoryResponse();
		
		dto.setId(inventory.getId());
		dto.setProductId(inventory.getProductId());
		dto.setTotalQuantity(inventory.getTotalQuantity());
		dto.setReservedQuantity(inventory.getReservedQuantity());
		dto.setAvailableQuantity(inventory.getAvailableQuantity());
		
		 return dto;
	}
}
