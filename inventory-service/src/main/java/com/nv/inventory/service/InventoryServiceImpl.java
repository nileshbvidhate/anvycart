package com.nv.inventory.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.nv.inventory.dto.InventoryCreateRequest;
import com.nv.inventory.dto.InternalInventoryResponse;
import com.nv.inventory.entity.Inventory;
import com.nv.inventory.exception.BusinessException;
import com.nv.inventory.exception.InsufficientStockException;
import com.nv.inventory.exception.ResourceAlreadyExistsException;
import com.nv.inventory.exception.ResourceNotFoundException;
import com.nv.inventory.mapper.InventoryMapper;
import com.nv.inventory.repository.InventoryRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class InventoryServiceImpl implements InventoryService {

	private final InventoryRepository inventoryRepository;

	private final InventoryMapper inventoryMapper;

	@Override
	@Transactional
	public void createInventory(Long productId,InventoryCreateRequest request) {

		if (inventoryRepository.findByProductId(productId).isPresent()) {
			throw new ResourceAlreadyExistsException("Inventory already exists");
		}

		Inventory inventory = new Inventory();
		inventory.setProductId(productId);
		inventory.setTotalQuantity(request.getTotalQuantity());
		inventory.setReservedQuantity(0);
		inventory.setAvailableQuantity(request.getTotalQuantity());

		inventoryRepository.save(inventory);
	}

	@Override
	@Transactional
	public InternalInventoryResponse getInventory(Long productId) {

		Inventory inventory = inventoryRepository.findByProductId(productId)
				.orElseThrow(() -> new ResourceNotFoundException("Inventory not found"));

		return inventoryMapper.toDto(inventory);
	}

	@Override
	@Transactional
	public void reserveStock(Long productId, int quantity) {

		Inventory inv = inventoryRepository.findByProductId(productId)
				.orElseThrow(() -> new ResourceNotFoundException("Inventory not found"));

		if (inv.getAvailableQuantity() < quantity) {
			throw new InsufficientStockException("Not enough stock");
		}

		inv.setReservedQuantity(inv.getReservedQuantity() + quantity);
		inv.setAvailableQuantity(inv.getAvailableQuantity() - quantity);

		inventoryRepository.save(inv);
	}

	@Override
	@Transactional
	public void confirmStock(Long productId, int quantity) {

		Inventory inv = inventoryRepository.findByProductId(productId)
				.orElseThrow(() -> new ResourceNotFoundException("Inventory not found"));

		if (inv.getReservedQuantity() < quantity) {
			throw new BusinessException("Not enough reserved stock to confirm");
		}

		inv.setReservedQuantity(inv.getReservedQuantity() - quantity);
		inv.setTotalQuantity(inv.getTotalQuantity() - quantity);

		// available remains same

		inventoryRepository.save(inv);
	}

	@Override
	@Transactional
	public void releaseStock(Long productId, int quantity) {

		Inventory inv = inventoryRepository.findByProductId(productId)
				.orElseThrow(() -> new ResourceNotFoundException("Inventory not found"));

		if (inv.getReservedQuantity() < quantity) {
			throw new BusinessException("Not enough reserved stock to release");
		}

		inv.setReservedQuantity(inv.getReservedQuantity() - quantity);
		inv.setAvailableQuantity(inv.getAvailableQuantity() + quantity);

		inventoryRepository.save(inv);
	}

	@Override
	@Transactional
	public void adjustStock(Long productId, int quantity) {

		Inventory inv = inventoryRepository.findByProductId(productId)
				.orElseThrow(() -> new ResourceNotFoundException("Inventory not found"));

		int newTotal = inv.getTotalQuantity() + quantity;

		if (newTotal < inv.getReservedQuantity()) {
			throw new BusinessException("Adjustment violates reserved stock");
		}

		inv.setTotalQuantity(newTotal);
		inv.setAvailableQuantity(newTotal - inv.getReservedQuantity());

		inventoryRepository.save(inv);
	}

}
