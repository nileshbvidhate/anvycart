package com.nv.delivery.dto.response;

import lombok.Data;
import java.util.Map;

@Data
public class NotificationEvent {

    private NotificationEventType eventType;
    private Long referenceId;
    private String email;
    private String mobile;
    private Map<String, Object> data;
}

