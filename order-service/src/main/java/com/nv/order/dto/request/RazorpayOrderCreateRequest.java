package com.nv.order.dto.request;

import java.math.BigDecimal;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RazorpayOrderCreateRequest {
	private Long orderId;
	private BigDecimal amount;
	private String currency;
}
