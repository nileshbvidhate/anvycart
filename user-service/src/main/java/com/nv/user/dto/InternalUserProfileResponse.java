package com.nv.user.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class InternalUserProfileResponse {

	private Long id;
    private Long userId;
    private String name;
    private String mobileNumber;
}
