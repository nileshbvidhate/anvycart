package com.nv.notification.enums;

public enum NotificationEventType {

    // Order lifecycle
    ORDER_PLACED,
    ORDER_CONFIRMED,
    ORDER_SHIPPED,
    ORDER_OUT_FOR_DELIVERY,
    ORDER_DELIVERED,
    ORDER_FAILED,

    // Payment lifecycle
    PAYMENT_SUCCESS,
    PAYMENT_FAILED
}
