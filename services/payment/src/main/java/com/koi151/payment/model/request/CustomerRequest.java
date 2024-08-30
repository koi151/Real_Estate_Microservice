package com.koi151.payment.model.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CustomerRequest (
    @NotBlank(message = "Account id is mandatory")
    String accountId,
    @NotBlank(message = "First name is mandatory")
    String firstName,
    @NotBlank(message = "Last name is mandatory")
    String lastName,
    @NotBlank(message = "Email is mandatory")
    String email,
    AddressRequest address
) {}
