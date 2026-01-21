package com.nv.delivery.publisher;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import com.nv.delivery.dto.response.NotificationEvent;

@Component
public class NotificationEventPublisher {

	private static final String TOPIC_NAME = "notification-events";

	private final KafkaTemplate<String, NotificationEvent> kafkaTemplate;

	public NotificationEventPublisher(KafkaTemplate<String, NotificationEvent> kafkaTemplate) {
		this.kafkaTemplate = kafkaTemplate;
	}

	public void publish(NotificationEvent event) {
		kafkaTemplate.send(TOPIC_NAME, event);
	}
}
