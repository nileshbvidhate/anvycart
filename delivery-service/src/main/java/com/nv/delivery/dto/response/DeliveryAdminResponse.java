package com.nv.delivery.dto.response;

import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;

@Getter
@Setter
public class DeliveryAdminResponse {

    private Long id;
    private Long orderId;
    private Long userId;
    private String deliveryStatus;
    private LocalDateTime expectedDeliveryAt;
    private LocalDateTime deliveredAt;
    private LocalDateTime createdAt;
}
