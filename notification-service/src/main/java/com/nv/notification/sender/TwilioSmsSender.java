package com.nv.notification.sender;

import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class TwilioSmsSender implements SmsSender {

    @Value("${twilio.from-number}")
    private String fromNumber;

    @Override
    public void send(String mobile, String message) {

        Message.creator(
                new PhoneNumber(mobile),     // TO
                new PhoneNumber(fromNumber), // FROM
                message                      // BODY
        ).create();
    }
}
