package com.nv.notification.dto;

import java.time.LocalDateTime;
import java.util.Map;

import com.nv.notification.enums.NotificationChannel;
import com.nv.notification.enums.NotificationEventType;

import lombok.Data;

@Data
public class NotificationEvent {

	// What happened (ORDER_PLACED, PAYMENT_SUCCESS, etc.)
	private NotificationEventType eventType;

	// Reference ID (orderId / paymentId / deliveryId)
	private Long referenceId;

	// Notification targets (optional based on channel)
	private String email;
	private String mobile;

	// Dynamic payload (productName, amount, status, etc.)
	private Map<String, Object> data;
}

/*
 * {
  "eventType": "ORDER_SHIPPED",
  "referenceId": 10234,
  "sourceService": "order-service",
  "occurredAt": "2026-01-20T16:45:00",
  "channel": "EMAIL",
  "email": "user@mail.com",
  "data": {
    "productName": "iPhone 15",
    "trackingId": "TRK123456",
    "estimatedDelivery": "2026-01-22"
  }
}

 */
