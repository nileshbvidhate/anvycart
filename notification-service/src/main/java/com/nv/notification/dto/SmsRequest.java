package com.nv.notification.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.Map;

@Data
public class SmsRequest {

    //Mobile number of the recipient
    @NotBlank(message="Mobile number should not be blank")
    private String mobile;

    //Template name (e.g. MOBILE_VERIFICATION)
    @NotBlank(message="Template should not be blank")
    private String template;

    //Dynamic values for SMS template
    @NotNull(message="variables should not be null")
    private Map<String, String> variables;
}
