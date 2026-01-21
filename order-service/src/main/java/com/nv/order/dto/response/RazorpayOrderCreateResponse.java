package com.nv.order.dto.response;

import java.math.BigDecimal;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RazorpayOrderCreateResponse {
	private String razorpayOrderId;
	private BigDecimal amount;
	private String currency;
}
