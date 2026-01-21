package com.nv.delivery.dto.response;

import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;

@Getter
@Setter
public class DeliveryUserResponse {

    private Long id;
    private Long orderId;
    private Long addressId;
    private String deliveryStatus;
    private LocalDateTime expectedDeliveryAt;
    private LocalDateTime deliveredAt;
}
