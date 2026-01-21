package com.nv.order.dto.response;

import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
@Builder
public class CreateOrderResponse {

    private Long orderId;
    private String status;

    private String razorpayOrderId;
    
    private BigDecimal amount;
    
    private String currency;
}