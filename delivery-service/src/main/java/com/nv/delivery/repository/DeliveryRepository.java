package com.nv.delivery.repository;

import com.nv.delivery.entity.Delivery;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface DeliveryRepository extends JpaRepository<Delivery, Long>, JpaSpecificationExecutor<Delivery> {

	Optional<Delivery> findByOrderId(Long orderId);
}
