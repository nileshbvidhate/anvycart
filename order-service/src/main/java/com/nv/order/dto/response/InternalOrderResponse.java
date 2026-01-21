package com.nv.order.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
public class InternalOrderResponse {

    private Long orderId;
    private Long userId;
    private Long addressId;
}

