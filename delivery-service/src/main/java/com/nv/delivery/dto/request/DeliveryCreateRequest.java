package com.nv.delivery.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DeliveryCreateRequest {

	@NotNull(message = "Order ID must not be null")
	private Long orderId;

	@NotNull(message = "User ID must not be null")
	private Long userId;

	@NotNull(message = "Address ID must not be null")
	private Long addressId;
}
