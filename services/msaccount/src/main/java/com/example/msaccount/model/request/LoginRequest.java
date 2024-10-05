package com.example.msaccount.model.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record LoginRequest (

    @NotNull(message = "Username is mandatory")
    String username,

    @NotNull(message = "Password is mandatory")
    String password
){}
