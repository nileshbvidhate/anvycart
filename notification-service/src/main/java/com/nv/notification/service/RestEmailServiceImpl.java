package com.nv.notification.service;

import com.nv.notification.dto.EmailRequest;
import com.nv.notification.sender.EmailSender;
import com.nv.notification.template.EmailTemplateResolver;

import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

import java.util.HashMap;
import java.util.Map;

@Service
public class RestEmailServiceImpl implements RestEmailService {

    private final EmailSender emailSender;
    private final EmailTemplateResolver templateResolver;
    private final SpringTemplateEngine templateEngine;

    public RestEmailServiceImpl(
            EmailSender emailSender,
            EmailTemplateResolver templateResolver,
            SpringTemplateEngine templateEngine
    ) {
        this.emailSender = emailSender;
        this.templateResolver = templateResolver;
        this.templateEngine = templateEngine;
    }

    @Override
    public void sendEmail(EmailRequest request) {

        Context context = new Context();

        Map<String, Object> variables = new HashMap<>();
        if (request.getVariables() != null) {
            variables.putAll(request.getVariables());
        }

        context.setVariables(variables);

        String templateName =
                templateResolver.resolveTemplateName(request.getTemplate());

        String subject =
                templateResolver.resolveSubject(request.getTemplate());

        String htmlBody =
                templateEngine.process(templateName, context);

        emailSender.sendHtml(
                request.getTo(),
                subject,
                htmlBody
        );
    }
}
