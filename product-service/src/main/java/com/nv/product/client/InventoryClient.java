package com.nv.product.client;

import com.nv.product.client.dto.InventoryCreateRequest;
import com.nv.product.dto.StockResponse;

public interface InventoryClient {

	public boolean createInventory(Long productId, InventoryCreateRequest inventoryCreateRequest);
	
	StockResponse getInventory(Long productId);
}
