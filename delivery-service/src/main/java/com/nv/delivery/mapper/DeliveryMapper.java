package com.nv.delivery.mapper;

import com.nv.delivery.dto.request.DeliveryCreateRequest;
import com.nv.delivery.dto.response.DeliveryAdminResponse;
import com.nv.delivery.dto.response.DeliveryUserResponse;
import com.nv.delivery.dto.response.InternalDeliveryResponse;
import com.nv.delivery.entity.Delivery;
import com.nv.delivery.entity.DeliveryStatus;
import org.springframework.stereotype.Component;
import java.time.LocalDateTime;

@Component
public class DeliveryMapper {

    public Delivery toEntity(DeliveryCreateRequest request) {

        LocalDateTime now = LocalDateTime.now();

        return Delivery.builder()
                .orderId(request.getOrderId())
                .userId(request.getUserId())
                .addressId(request.getAddressId())
                .deliveryStatus(DeliveryStatus.PENDING)
                .expectedDeliveryAt(LocalDateTime.now().plusDays(5))
                .createdAt(now)
                .updatedAt(now)
                .build();
    }
    
    public InternalDeliveryResponse toInternalResponse(Delivery delivery) {

        InternalDeliveryResponse response = new InternalDeliveryResponse();
        response.setId(delivery.getId());
        response.setOrderId(delivery.getOrderId());
        response.setAddressId(delivery.getAddressId());
        response.setDeliveryStatus(delivery.getDeliveryStatus());
        response.setExpectedDeliveryAt(delivery.getExpectedDeliveryAt());
        response.setDeliveredAt(delivery.getDeliveredAt());

        return response;
    }
    
    public DeliveryAdminResponse toAdminResponse(Delivery delivery) {

        DeliveryAdminResponse response = new DeliveryAdminResponse();
        response.setId(delivery.getId());
        response.setOrderId(delivery.getOrderId());
        response.setUserId(delivery.getUserId());
        response.setDeliveryStatus(delivery.getDeliveryStatus().name());
        response.setExpectedDeliveryAt(delivery.getExpectedDeliveryAt());
        response.setDeliveredAt(delivery.getDeliveredAt());
        response.setCreatedAt(delivery.getCreatedAt());

        return response;
    }
    
    public DeliveryUserResponse toUserResponse(Delivery delivery) {

        DeliveryUserResponse response = new DeliveryUserResponse();
        response.setId(delivery.getId());
        response.setOrderId(delivery.getOrderId());
        response.setAddressId(delivery.getAddressId());
        response.setDeliveryStatus(delivery.getDeliveryStatus().name());
        response.setExpectedDeliveryAt(delivery.getExpectedDeliveryAt());
        response.setDeliveredAt(delivery.getDeliveredAt());

        return response;
    }
}
