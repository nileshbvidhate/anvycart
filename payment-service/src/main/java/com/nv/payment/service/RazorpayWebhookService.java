package com.nv.payment.service;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import com.nv.payment.client.OrderFeignClient;
import com.nv.payment.entity.Payment;
import com.nv.payment.entity.PaymentStatus;
import com.nv.payment.exception.ResourceNotFoundException;
import com.nv.payment.repository.PaymentRepository;
import com.nv.payment.util.RazorpaySignatureUtil;

@Service
public class RazorpayWebhookService {

	private final PaymentRepository paymentRepository;
	private final OrderFeignClient orderFeignClient;

	private final String webhookSecret;

	public RazorpayWebhookService(PaymentRepository paymentRepository, OrderFeignClient orderClient,
			@Value("${razorpay.webhook-secret}") String webhookSecret) {

		this.paymentRepository = paymentRepository;
		this.orderFeignClient = orderClient;
		this.webhookSecret = webhookSecret;
	}

	public void processWebhook(String payload, String signature) {

		// 1. Verify webhook signature (payload + secret)
		boolean valid = RazorpaySignatureUtil.verify(payload, webhookSecret, signature);

		if (!valid) {
			return; // ignore invalid webhook
		}

		JSONObject json = new JSONObject(payload);
		String event = json.getString("event");

		// 2. Handle payment captured
		if ("payment.captured".equals(event)) {
			handleCaptured(json, signature);
		}

		// 3. Handle payment failed
		if ("payment.failed".equals(event)) {
			handleFailed(json);
		}
	}

	private void handleCaptured(JSONObject json, String signature) {

		JSONObject paymentEntity = json.getJSONObject("payload").getJSONObject("payment").getJSONObject("entity");

		String razorpayOrderId = paymentEntity.getString("order_id");
		String razorpayPaymentId = paymentEntity.getString("id");
		String paymentMethod = paymentEntity.getString("method");

		Payment payment = paymentRepository.findByRazorpayOrderId(razorpayOrderId)
				.orElseThrow(() -> new ResourceNotFoundException("Razorpay order not found"));

		payment.setStatus(PaymentStatus.CAPTURED);
		payment.setRazorpayPaymentId(razorpayPaymentId);
		payment.setPaymentMethod(paymentMethod);

		paymentRepository.save(payment);

		// Notify Order Service
		orderFeignClient.handlePaymentSuccess(payment.getOrderId());
	}

	private void handleFailed(JSONObject json) {

		JSONObject paymentEntity = json.getJSONObject("payload").getJSONObject("payment").getJSONObject("entity");

		String razorpayOrderId = paymentEntity.getString("order_id");
		String razorpayPaymentId = paymentEntity.getString("id");
		String paymentMethod = paymentEntity.getString("method");

		Payment payment = paymentRepository.findByRazorpayOrderId(razorpayOrderId).orElseThrow();

		if (payment.getStatus() == PaymentStatus.CAPTURED) {
			return;
		}

		payment.setStatus(PaymentStatus.FAILED);
		payment.setRazorpayPaymentId(razorpayPaymentId);
		payment.setPaymentMethod(paymentMethod);

		paymentRepository.save(payment);

		orderFeignClient.handlePaymentFailed(payment.getOrderId());
	}

}
