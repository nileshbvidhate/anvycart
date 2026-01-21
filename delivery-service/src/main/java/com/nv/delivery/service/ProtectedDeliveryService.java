package com.nv.delivery.service;

import com.nv.delivery.dto.response.DeliveryUserResponse;

public interface ProtectedDeliveryService {

    DeliveryUserResponse getDeliveryByOrderId(Long orderId, Long userId);
}
