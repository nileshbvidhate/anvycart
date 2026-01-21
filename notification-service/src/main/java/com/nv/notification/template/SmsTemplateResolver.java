package com.nv.notification.template;

import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class SmsTemplateResolver {

	public String resolveMessage(String template, Map<String, String> vars) {

		return switch (template) {

		case "MOBILE_VERIFICATION" ->
			"Your AnvyCart verification OTP is " + vars.get("otp") + ". Do not share it with anyone.";

		case "FORGOT_PASSWORD" -> "Use OTP " + vars.get("otp") + " to reset your AnvyCart password.";

		default -> "AnvyCart notification.";
		};
	}
}
