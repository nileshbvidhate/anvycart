package com.nv.delivery.dto.response;

import com.nv.delivery.entity.DeliveryStatus;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;

@Getter
@Setter
public class InternalDeliveryResponse {

    private Long id;
    private Long orderId;
    private Long addressId;
    private DeliveryStatus deliveryStatus;
    private LocalDateTime expectedDeliveryAt;
    private LocalDateTime deliveredAt;
}
