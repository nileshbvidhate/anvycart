package com.nv.notification.service;

import com.nv.notification.dto.NotificationEvent;
import com.nv.notification.sender.EmailSender;
import com.nv.notification.template.EmailTemplateResolver;

import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

import java.util.HashMap;
import java.util.Map;

@Service
public class EventEmailServiceImpl implements EventEmailService {

	private final EmailSender emailSender;
	private final EmailTemplateResolver templateResolver;
	private final SpringTemplateEngine templateEngine;

	public EventEmailServiceImpl(EmailSender emailSender, EmailTemplateResolver templateResolver,
			SpringTemplateEngine templateEngine) {
		this.emailSender = emailSender;
		this.templateResolver = templateResolver;
		this.templateEngine = templateEngine;
	}

	@Override
	public void handleEvent(NotificationEvent event) {

		// 1️⃣ Skip if email is not present
		if (event.getEmail() == null || event.getEmail().isBlank()) {
			return;
		}

		// 2️⃣ Prepare Thymeleaf context
		Context context = new Context();

		Map<String, Object> variables = new HashMap<>();
		if (event.getData() != null) {
			variables.putAll(event.getData());
		}

		context.setVariables(variables);

		// 3️⃣ Resolve template & subject using eventType
		String templateKey = event.getEventType().name();

		String templateName = templateResolver.resolveTemplateName(templateKey);

		String subject = templateResolver.resolveSubject(templateKey);

		// 4️⃣ Render HTML
		String htmlBody = templateEngine.process(templateName, context);

		// 5️⃣ Send email
		emailSender.sendHtml(event.getEmail(), subject, htmlBody);
	}
}
