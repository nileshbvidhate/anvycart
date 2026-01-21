package com.nv.notification.sender;

public interface EmailSender {

    void sendHtml(String to, String subject, String htmlBody);
}
