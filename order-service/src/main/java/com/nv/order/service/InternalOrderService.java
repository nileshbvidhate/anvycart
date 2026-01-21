package com.nv.order.service;

import com.nv.order.dto.response.InternalOrderResponse;

public interface InternalOrderService {

    void handlePaymentSuccess(Long orderId);
    
    void handlePaymentFailed(Long orderId);
    
    InternalOrderResponse getOrderById(Long orderId);
    
    void markOrderShipped(Long orderId);
    
    void markOutForDelivery(Long orderId);
    
    void markDelivered(Long orderId);
    
    void markCancelledByDelivery(Long orderId);
}
