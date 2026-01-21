package com.nv.delivery.service.impl;

import com.nv.delivery.client.OrderFeignClient;
import com.nv.delivery.dto.response.DeliveryAdminResponse;
import com.nv.delivery.dto.response.NotificationEvent;
import com.nv.delivery.dto.response.NotificationEventType;
import com.nv.delivery.entity.Delivery;
import com.nv.delivery.entity.DeliveryStatus;
import com.nv.delivery.exception.BadRequestException;
import com.nv.delivery.exception.ResourceNotFoundException;
import com.nv.delivery.mapper.DeliveryMapper;
import com.nv.delivery.publisher.NotificationEventPublisher;
import com.nv.delivery.repository.DeliveryAdminSpecification;
import com.nv.delivery.repository.DeliveryRepository;
import com.nv.delivery.service.AdminDeliveryService;
import feign.FeignException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class AdminDeliveryServiceImpl implements AdminDeliveryService {

	private final DeliveryRepository deliveryRepository;
	private final OrderFeignClient orderFeignClient;
	private final DeliveryMapper deliveryMapper;
	private final NotificationEventPublisher eventPublisher;

	@Override
	@Transactional
	public void updateDeliveryStatus(Long deliveryId, DeliveryStatus newStatus) {

		Delivery delivery = deliveryRepository.findById(deliveryId)
				.orElseThrow(() -> new ResourceNotFoundException("Delivery not found with id: " + deliveryId));

		DeliveryStatus currentStatus = delivery.getDeliveryStatus();

		if (currentStatus == DeliveryStatus.CANCELLED) {
			throw new BadRequestException("Cancelled delivery cannot be updated");
		}

		// Idempotent
		if (currentStatus == newStatus) {
			return;
		}

		try {

			switch (currentStatus) {

			case PENDING -> {
				if (newStatus != DeliveryStatus.SHIPPED) {
					throw new BadRequestException("Invalid delivery status transition");
				}
//				orderFeignClient.markOrderShipped(delivery.getOrderId());
				delivery.setDeliveryStatus(DeliveryStatus.SHIPPED);
			}

			case SHIPPED -> {
				if (newStatus != DeliveryStatus.OUT_FOR_DELIVERY) {
					throw new BadRequestException("Invalid delivery status transition");
				}
//				orderFeignClient.markOrderOutForDelivery(delivery.getOrderId());
				delivery.setDeliveryStatus(DeliveryStatus.OUT_FOR_DELIVERY);
			}

			case OUT_FOR_DELIVERY -> {
				if (newStatus != DeliveryStatus.DELIVERED) {
					throw new BadRequestException("Invalid delivery status transition");
				}
//				orderFeignClient.markOrderDelivered(delivery.getOrderId());
				delivery.setDeliveryStatus(DeliveryStatus.DELIVERED);
				delivery.setDeliveredAt(LocalDateTime.now());

				// 🔔 Publish ORDER_DELIVERED event
				NotificationEvent event = new NotificationEvent();
				event.setEventType(NotificationEventType.ORDER_DELIVERED);
				event.setReferenceId(delivery.getOrderId());
				event.setEmail("nileshvidhate2020@gmail.com"); // must exist in entity

				Map<String, Object> data = new HashMap<>();
				data.put("orderId", delivery.getOrderId());

				event.setData(data);

				eventPublisher.publish(event);
			}

			default -> throw new BadRequestException("Invalid delivery status transition");
			}

		} catch (FeignException ex) {
			// rollback delivery update
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
					"Failed to update order status while updating delivery status", ex);
		}
	}

	@Override
	@Transactional
	public void updateExpectedDeliveryDate(Long deliveryId, LocalDateTime expectedDeliveryAt) {

		Delivery delivery = deliveryRepository.findById(deliveryId)
				.orElseThrow(() -> new ResourceNotFoundException("Delivery not found with id: " + deliveryId));

		if (delivery.getDeliveryStatus() == DeliveryStatus.DELIVERED) {
			throw new BadRequestException("Expected delivery time cannot be updated for delivered delivery");
		}

		if (delivery.getDeliveryStatus() == DeliveryStatus.CANCELLED) {
			throw new BadRequestException("Expected delivery time cannot be updated for cancelled delivery");
		}

		if (expectedDeliveryAt.isBefore(LocalDateTime.now())) {
			throw new BadRequestException("Expected delivery time cannot be in the past");
		}

		delivery.setExpectedDeliveryAt(expectedDeliveryAt);
		// JPA dirty checking handles persistence
	}

	@Override
	@Transactional(readOnly = true)
	public Page<DeliveryAdminResponse> getDeliveries(DeliveryStatus status, Long orderId, Long userId,
			LocalDateTime fromDate, LocalDateTime toDate, Pageable pageable) {

		Specification<Delivery> specification = DeliveryAdminSpecification.build(status, orderId, userId, fromDate,
				toDate);

		return deliveryRepository.findAll(specification, pageable).map(deliveryMapper::toAdminResponse);
	}

}
