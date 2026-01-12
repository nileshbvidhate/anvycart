package com.nv.user.dto;

import lombok.Data;

@Data
public class PatchAddressRequest {

    private String addressLine1;
    private String addressLine2;
    private String city;
    private String state;
    private String postalCode;
    private String country;
    private Boolean isDefault;
}
