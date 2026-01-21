package com.nv.notification.sender;

public interface SmsSender {

    void send(String mobile, String message);
}
