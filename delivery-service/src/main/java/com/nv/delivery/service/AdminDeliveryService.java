package com.nv.delivery.service;

import java.time.LocalDateTime;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.nv.delivery.dto.response.DeliveryAdminResponse;
import com.nv.delivery.entity.DeliveryStatus;

public interface AdminDeliveryService {

	void updateDeliveryStatus(Long deliveryId, DeliveryStatus newStatus);

	void updateExpectedDeliveryDate(Long deliveryId, LocalDateTime expectedDeliveryAt);

	Page<DeliveryAdminResponse> getDeliveries(DeliveryStatus status, Long orderId, Long userId, LocalDateTime fromDate,
			LocalDateTime toDate, Pageable pageable);

}
