package com.nv.notification.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.Map;

@Data
public class EmailRequest {

	//Email address of the recipient
	@NotBlank(message="Email address should not be blank")
	private String to;

	//Template name (e.g. EMAIL_VERIFICATION)
	@NotBlank(message="Template should not be blank")
	private String template;

	//Dynamic values used inside template Example: { "otp": "123456" }
	@NotNull(message="variables should not be null")
	private Map<String, Object> variables;
}
