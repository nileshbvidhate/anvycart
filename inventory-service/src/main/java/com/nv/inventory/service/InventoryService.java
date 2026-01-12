package com.nv.inventory.service;

import com.nv.inventory.dto.InventoryCreateRequest;
import com.nv.inventory.dto.InternalInventoryResponse;

public interface InventoryService {
	
	public void createInventory(Long productId, InventoryCreateRequest request);
	
	public InternalInventoryResponse getInventory(Long productId);
	
	public void reserveStock(Long productId, int quantity);
	
	public void confirmStock(Long productId, int quantity);
	
	public void releaseStock(Long productId, int quantity);
	
	// Admin only service
	public void adjustStock(Long productId, int quantity);
}
