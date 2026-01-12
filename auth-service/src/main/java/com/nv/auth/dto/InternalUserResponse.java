package com.nv.auth.dto;

import lombok.Data;

@Data
public class InternalUserResponse {
	private Long id;
    private String username;
    private UserRole role;
    private AccountStatus status;
    private Boolean isEmailVerified;
}
