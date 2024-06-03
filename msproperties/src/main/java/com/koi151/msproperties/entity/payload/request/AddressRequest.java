package com.koi151.msproperties.entity.payload.request;

import jakarta.persistence.Column;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AddressRequest {
    @Column(name = "city", nullable = false, length = 50)
    @NotEmpty(message = "City cannot be empty")
    private String city;

    @Column(name = "district", nullable = false, length = 50)
    @NotEmpty(message = "District cannot be empty")
    private String district;

    @Column(name = "ward", nullable = false, length = 50)
    @NotEmpty(message = "Ward cannot be empty")
    private String ward;

    @Column(name = "street_address", length = 150)
    private String streetAddress;
}
