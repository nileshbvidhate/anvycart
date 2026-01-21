package com.nv.notification.consumer;

import com.nv.notification.dto.NotificationEvent;
import com.nv.notification.service.EventEmailService;

import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class NotificationEventConsumer {

	private final EventEmailService eventEmailService;

	@KafkaListener(topics = "notification-events", groupId = "notification-service")
	public void consume(NotificationEvent event) {

		System.out.println("Received event: " + event.getEventType());

		eventEmailService.handleEvent(event);
	}
}
