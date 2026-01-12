package com.nv.auth.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class PasswordReset {
	@NotBlank(message="current password is required")
	private String currentPassword;
	
	@NotBlank(message="new password is required")
	@Size(min = 8, message = "Password must be at least 8 characters")
	private String newPassword;
}
