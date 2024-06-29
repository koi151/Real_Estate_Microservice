package com.example.msaccount.model.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class AccountLoginRequest {

    @NotBlank(message = "User name cannot be blank")
    private String userName;

    @NotBlank(message = "Password cannot be blank")
    private String password;
}
