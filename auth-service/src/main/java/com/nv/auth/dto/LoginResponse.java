package com.nv.auth.dto;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class LoginResponse {
    private String token;
    private String tokenType;
}
