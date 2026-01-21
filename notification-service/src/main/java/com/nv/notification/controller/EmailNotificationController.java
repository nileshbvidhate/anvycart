package com.nv.notification.controller;

import com.nv.notification.dto.ApiResponse;
import com.nv.notification.dto.EmailRequest;
import com.nv.notification.service.RestEmailService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/internal/notifications/email")
public class EmailNotificationController {

	private final RestEmailService emailService;

	public EmailNotificationController(RestEmailService emailService) {
		this.emailService = emailService;
	}

	@PostMapping
	public ResponseEntity<ApiResponse<Void>> sendEmail(@Valid @RequestBody EmailRequest request) {

		emailService.sendEmail(request);

		return ResponseEntity.ok(new ApiResponse<>(HttpStatus.OK.value(), "Email sent successfully", null));
	}
}
