package com.example.msaccount.model.request;

import com.example.msaccount.enums.AccountStatusEnum;
import com.example.msaccount.enums.AccountTypeEnum;
import jakarta.validation.constraints.*;

public record AccountCreateRequest(

    AccountTypeEnum accountType,

//    @NotNull(message = "Role ID cannot be null")
//    Long roleId,

    @NotBlank(message = "User name is mandatory")
    @Size(min = 5, max = 30, message = "User name length must be between {min} and {max} characters")
    @Pattern(regexp = "[A-Za-z0-9.\\s]+", message = "Account name contains invalid characters")
    String accountName,

    @NotBlank(message = "Phone number is mandatory")
    String phone,

    AccountStatusEnum status,

    @NotBlank(message = "First name is mandatory")
    @Size(min = 1, max = 30, message = "First name length must be between {min} and {max} characters")
    @Pattern(regexp = "[A-Za-z.\\s]+", message = "First name contains invalid characters")
    String firstName,

    @NotBlank(message = "Last name is mandatory")
    @Size(min = 1, max = 70, message = "Last name length must be between {min} and {max} characters")
    @Pattern(regexp = "[A-Za-z.\\s]+", message = "Last name contains invalid characters")
    String lastName,

    @NotBlank(message = "Password cannot be blank")
    @Size(min = 6, max = 100, message = "Password length must be between {min} and {max} characters")
    @Pattern(regexp = "^(?=.*[A-Z])(?=.*[!@#$%^&*]).*$",
        message = "Password must include at least one uppercase letter and one special character")
    String password,

    @NotBlank(message = "Password retype is mandatory")
    String retypePassword,

    @Email(message = "Invalid email")
    @NotBlank(message = "Email cannot be blank")
    String email

//    long facebookAccountId,
//
//    long googleAccountId
) { }