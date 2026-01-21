package com.nv.delivery.service.impl;

import com.nv.delivery.dto.response.DeliveryUserResponse;
import com.nv.delivery.entity.Delivery;
import com.nv.delivery.exception.ResourceNotFoundException;
import com.nv.delivery.mapper.DeliveryMapper;
import com.nv.delivery.repository.DeliveryRepository;
import com.nv.delivery.service.ProtectedDeliveryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ProtectedDeliveryServiceImpl implements ProtectedDeliveryService {

	private final DeliveryRepository deliveryRepository;
	private final DeliveryMapper deliveryMapper;

	@Override
	@Transactional(readOnly = true)
	public DeliveryUserResponse getDeliveryByOrderId(Long orderId, Long userId) {

		Delivery delivery = deliveryRepository.findByOrderId(orderId)
				.orElseThrow(() -> new ResourceNotFoundException("Delivery not found for orderId: " + orderId));

		// Ownership validation
		if (!delivery.getUserId().equals(userId)) {
			throw new ResourceNotFoundException("Delivery not found for this user");
		}

		return deliveryMapper.toUserResponse(delivery);
	}
}
