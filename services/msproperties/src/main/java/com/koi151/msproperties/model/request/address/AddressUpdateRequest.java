package com.koi151.msproperties.model.request.address;

import jakarta.persistence.Column;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

public record AddressUpdateRequest (
    @NotBlank(message = "City name is mandatory")
    @Size(max = 50, message = "City name cannot exceed {max} characters length")
    String city,

    @NotBlank(message = "District name is mandatory")
    @Size(max = 50, message = "District name cannot exceed {max} characters length")
    String district,

    @NotEmpty(message = "Ward name is mandatory")
    @Size(max = 50, message = "Ward name cannot exceed {max} characters length")
    String ward,

    @Column(name = "street_address", length = 150)
    @Size(max = 150, message = "Street address cannot exceed {max} characters length")
    String streetAddress
){}
