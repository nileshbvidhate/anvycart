package com.nv.notification.service;

import com.nv.notification.dto.EmailRequest;

public interface RestEmailService {
	
    void sendEmail(EmailRequest request);
}
