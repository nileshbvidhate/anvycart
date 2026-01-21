package com.nv.notification.controller;

import com.nv.notification.dto.ApiResponse;
import com.nv.notification.dto.SmsRequest;
import com.nv.notification.service.SmsService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/internal/notifications/sms")
public class SmsNotificationController {

	private final SmsService smsService;

	public SmsNotificationController(SmsService smsService) {
		this.smsService = smsService;
	}

	@PostMapping
	public ResponseEntity<ApiResponse<Void>> sendSms(@Valid @RequestBody SmsRequest request) {

		smsService.sendSms(request);

		return ResponseEntity.ok(new ApiResponse<>(HttpStatus.OK.value(), "SMS sent successfully", null));
	}
}
