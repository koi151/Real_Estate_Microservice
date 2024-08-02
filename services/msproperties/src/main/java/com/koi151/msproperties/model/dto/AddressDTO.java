package com.koi151.msproperties.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@AllArgsConstructor
public class AddressDTO {
    private String city;
    private String district;
    private String ward;
    private String street;
}
