package com.nv.order.entity;

public enum OrderStatus {
    PAYMENT_PENDING,
    PAYMENT_PROCESSING,
    PAYMENT_FAILED,
    PLACED,
    CONFIRMED,
    CANCELLED,	
    SHIPPED,
    OUT_FOR_DELIVERY,
    DELIVERED
}
