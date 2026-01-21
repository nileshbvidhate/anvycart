package com.nv.notification.service;

import com.nv.notification.dto.NotificationEvent;

public interface EventEmailService {
	
	void handleEvent(NotificationEvent event);
	
}
