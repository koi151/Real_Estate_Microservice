package com.example.msaccount.model.request.admin;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import java.util.List;

public record AccountUpdateRequest (

    @NotBlank(message = "Account id is mandatory")
    String accountId,

    List<String> roleNames,

    @Pattern(regexp = "[A-Za-z0-9.\\s]+", message = "Username contains invalid characters")
    String username,

    String phone,

//    @Enumerated(EnumType.STRING)
//      AccountStatusEnum status;
    boolean accountEnable,

    @Size(min = 1, max = 30, message = "First name length must be between {min} and {max} characters")
    @Pattern(regexp = "[A-Za-z.\\s]+", message = "First name contains invalid characters")
    String firstName,

    @Size(min = 1, max = 70, message = "Last name length must be between {min} and {max} characters")
    @Pattern(regexp = "[A-Za-z.\\s]+", message = "Last name contains invalid characters")
    String lastName,

    @Size(min = 6, max = 100, message = "Password length must be between {min} and {max} characters")
    @Pattern(regexp = "^(?=.*[A-Z])(?=.*[!@#$%^&*]).*$",
            message = "Password must include at least one uppercase letter and one special character")
    String password,

    @Email(message = "Invalid email")
    String email,
    boolean avatarUrlRemove
) {}
