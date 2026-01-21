package com.nv.payment.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.nv.payment.service.RazorpayWebhookService;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/webhooks")
@RequiredArgsConstructor
public class RazorpayWebhookController {

	private final RazorpayWebhookService webhookService;

	@PostMapping("/razorpay")
	public ResponseEntity<String> handleWebhook(@RequestBody String payload,
			@RequestHeader("X-Razorpay-Signature") String signature) {

		webhookService.processWebhook(payload, signature);

		return ResponseEntity.ok("OK");
	}
}
