package com.nv.order.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderPaymentVerifyRequest {

    @NotBlank(message="razorpayOrderId should not be null")
    private String razorpayOrderId;

    @NotBlank(message="razorpayPaymentId should not be null")
    private String razorpayPaymentId;

    @NotBlank(message="razorpaySignature should not be null")
    private String razorpaySignature;
}
