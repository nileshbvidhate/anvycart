package com.nv.notification.service;

import com.nv.notification.dto.SmsRequest;
import com.nv.notification.sender.SmsSender;
import com.nv.notification.template.SmsTemplateResolver;
import org.springframework.stereotype.Service;

@Service
public class SmsServiceImpl implements SmsService {

	private final SmsSender smsSender;
	private final SmsTemplateResolver templateResolver;

	public SmsServiceImpl(SmsSender smsSender, SmsTemplateResolver templateResolver) {
		this.smsSender = smsSender;
		this.templateResolver = templateResolver;
	}

	@Override
	public void sendSms(SmsRequest request) {

		String message = templateResolver.resolveMessage(request.getTemplate(), request.getVariables());

		smsSender.send(request.getMobile(), message);
	}
}
