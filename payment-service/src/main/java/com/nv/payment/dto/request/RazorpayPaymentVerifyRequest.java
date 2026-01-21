package com.nv.payment.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RazorpayPaymentVerifyRequest {

    @NotBlank(message="razorpayOrderId should not be blanck")
    private String razorpayOrderId;

    @NotBlank(message="razorpayPaymentId should not be blanck")
    private String razorpayPaymentId;

    @NotBlank(message="razorpaySignature should not be blanck")
    private String razorpaySignature;
}

