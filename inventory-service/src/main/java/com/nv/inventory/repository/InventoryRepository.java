package com.nv.inventory.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import com.nv.inventory.entity.Inventory;


public interface InventoryRepository extends JpaRepository<Inventory, Long> {

	//Fetch inventory by productId
	Optional<Inventory> findByProductId(Long productId);

}
