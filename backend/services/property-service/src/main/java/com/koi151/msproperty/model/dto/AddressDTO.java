package com.koi151.msproperty.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AddressDTO {
    private String city;
    private String district;
    private String ward;
    private String street;
}
