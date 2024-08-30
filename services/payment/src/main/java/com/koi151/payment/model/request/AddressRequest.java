package com.koi151.payment.model.request;

import jakarta.validation.constraints.NotBlank;

public record AddressRequest (
    @NotBlank(message = "City name is mandatory in address")
    String city,
    @NotBlank(message = "District name is mandatory in address")
    String district,
    @NotBlank(message = "Ward name is mandatory in address")
    String ward,
    @NotBlank(message = "Street information is mandatory in address")
    String streetAddress
) {}
