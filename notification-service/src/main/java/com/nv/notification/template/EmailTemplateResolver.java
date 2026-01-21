package com.nv.notification.template;

import org.springframework.stereotype.Component;

@Component
public class EmailTemplateResolver {

	public String resolveTemplateName(String templateKey) {

		return switch (templateKey) {

		// ===== REST BASED EMAILS =====
		case "FORGOT_PASSWORD" -> "email/forgot-password";
		case "EMAIL_VERIFICATION" -> "email/email-verification";

		// ===== EVENT BASED EMAILS =====
		case "ORDER_PLACED" -> "email/order-placed";
		case "ORDER_FAILED" -> "email/order-failed";
		case "ORDER_CONFIRMED" -> "email/order-confirmed";
		case "ORDER_SHIPPED" -> "email/order-shipped";
		case "ORDER_OUT_FOR_DELIVERY" -> "email/order-out-for-delivery";
		case "ORDER_DELIVERED" -> "email/order-delivered";
		case "PAYMENT_SUCCESS" -> "email/payment-success";
		case "PAYMENT_FAILED" -> "email/payment-failed";

		default -> "email/default";
		};
	}

	public String resolveSubject(String templateKey) {

		return switch (templateKey) {

		// ===== REST BASED EMAILS =====
		case "FORGOT_PASSWORD" -> "Reset Your AnvyCart Password – OTP";

		case "EMAIL_VERIFICATION" -> "Verify Your AnvyCart Email";

		// ===== EVENT BASED EMAILS =====
		case "ORDER_PLACED" -> "Order Placed Successfully – AnvyCart";
		
		case "ORDER_FAILED" -> "Order Failed – AnvyCart";

		case "ORDER_CONFIRMED" -> "Order Confirmed – AnvyCart";

		case "ORDER_SHIPPED" -> "Your Order Has Been Shipped – AnvyCart";

		case "ORDER_OUT_FOR_DELIVERY" -> "Out for Delivery – AnvyCart";

		case "ORDER_DELIVERED" -> "Order Delivered – AnvyCart";

		case "PAYMENT_SUCCESS" -> "Payment Successful – AnvyCart";

		case "PAYMENT_FAILED" -> "Payment Failed – AnvyCart";

		default -> "AnvyCart Notification";
		};
	}
}
