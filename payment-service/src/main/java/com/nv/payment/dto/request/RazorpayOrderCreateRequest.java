package com.nv.payment.dto.request;

import java.math.BigDecimal;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class RazorpayOrderCreateRequest {
	
	@NotNull(message="orderId should not be null")
	private Long orderId;
	
	@NotNull(message="amount should not be null")
	@Positive(message="amount should be positive")
	private BigDecimal amount;

	@NotBlank(message="currency is required")
	private String currency;
}
