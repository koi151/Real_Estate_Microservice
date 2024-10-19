package com.koi151.msproperty.model.request.address;

import jakarta.validation.constraints.Size;

public record AddressSearchRequest (
        @Size(max = 50, message = "City search cannot exceed {max} characters length")
        String city,

        @Size(max = 50, message = "District search cannot exceed {max} characters length")
        String district,

        @Size(max = 50, message = "Ward search cannot exceed {max} characters length")
        String ward,

        @Size(max = 150, message = "Street address search cannot exceed {max} characters length")
        String streetAddress
){}
