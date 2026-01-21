package com.nv.notification.service;

import com.nv.notification.dto.SmsRequest;

public interface SmsService {

    void sendSms(SmsRequest request);
}
