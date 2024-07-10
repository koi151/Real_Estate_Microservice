package com.koi151.msproperties.model.request;

import jakarta.persistence.Column;
import jakarta.validation.constraints.NotEmpty;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AddressCreateRequest {

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
