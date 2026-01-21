package com.nv.delivery.service;

import com.nv.delivery.dto.request.DeliveryCreateRequest;
import com.nv.delivery.dto.response.InternalDeliveryResponse;

public interface InternalDeliveryService {

    void createDelivery(DeliveryCreateRequest request);
    
    InternalDeliveryResponse getDeliveryByOrderId(Long orderId);
    
    void cancelDeliveryByOrderId(Long orderId);

}
