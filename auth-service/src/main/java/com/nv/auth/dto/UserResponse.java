package com.nv.auth.dto;

import java.time.LocalDateTime;
import lombok.Data;

@Data
public class UserResponse {
    private Long id;
    private String username;
    private UserRole role;
    private AccountStatus status;
    private Boolean isEmailVerified;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
