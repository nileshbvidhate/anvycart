package com.nv.user.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PatchUserProfileRequest {

    private String name;
    private String gender;
    private String mobileNumber;
    private String altMobileNumber;
}