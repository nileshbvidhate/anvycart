package com.nv.delivery.service.impl;

import com.nv.delivery.dto.request.DeliveryCreateRequest;
import com.nv.delivery.dto.response.InternalDeliveryResponse;
import com.nv.delivery.entity.Delivery;
import com.nv.delivery.entity.DeliveryStatus;
import com.nv.delivery.exception.ResourceNotFoundException;
import com.nv.delivery.mapper.DeliveryMapper;
import com.nv.delivery.repository.DeliveryRepository;
import com.nv.delivery.service.InternalDeliveryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class InternalDeliveryServiceImpl implements InternalDeliveryService {

	private final DeliveryRepository deliveryRepository;
	private final DeliveryMapper deliveryMapper;

	@Override
	@Transactional
	public void createDelivery(DeliveryCreateRequest request) {

		Delivery delivery = deliveryMapper.toEntity(request);

		deliveryRepository.save(delivery);
	}

	@Override
	@Transactional(readOnly = true)
	public InternalDeliveryResponse getDeliveryByOrderId(Long orderId) {

		Delivery delivery = deliveryRepository.findByOrderId(orderId)
				.orElseThrow(() -> new ResourceNotFoundException("Delivery not found for orderId: " + orderId));

		return deliveryMapper.toInternalResponse(delivery);
	}

	@Override
	@Transactional
	public void cancelDeliveryByOrderId(Long orderId) {

		Delivery delivery = deliveryRepository.findByOrderId(orderId)
				.orElseThrow(() -> new ResourceNotFoundException("Delivery not found for orderId: " + orderId));

		// Idempotent cancel
		if (delivery.getDeliveryStatus() == DeliveryStatus.CANCELLED) {
			return;
		}

		// Business rule: delivered delivery cannot be cancelled
		if (delivery.getDeliveryStatus() == DeliveryStatus.DELIVERED) {
			throw new IllegalStateException("Delivered delivery cannot be cancelled");
		}

		delivery.setDeliveryStatus(DeliveryStatus.CANCELLED);
		// updatedAt handled by @PreUpdate
	}
}
