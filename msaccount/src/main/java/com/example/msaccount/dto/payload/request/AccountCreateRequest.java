package com.example.msaccount.dto.payload.request;

import com.example.msaccount.entity.AccountStatusEnum;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AccountCreateRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int accountId;

    @NotNull(message = "User name cannot be null")
    @Pattern(regexp = "[A-Za-z0-9.\\s]+", message = "Username contains invalid characters")
    private String userName;

    @NotNull(message = "Phone number cannot be null")
    private String phone;

    @Enumerated(EnumType.STRING)
    @NotNull(message = "Account status cannot be null")
    private AccountStatusEnum status;

    @Size(min = 1, max = 30, message = "First name length must be between {min} and {max} characters")
    @Pattern(regexp = "[A-Za-z.\\s]+", message = "First name contains invalid characters")
    private String firstName;

    @Size(min = 1, max = 70, message = "Last name length must be between {min} and {max} characters")
    @Pattern(regexp = "[A-Za-z.\\s]+", message = "Last name contains invalid characters")
    private String lastName;

    @NotNull(message = "Password cannot be null")
    @Size(min = 6, max = 100, message = "Password length must be between {min} and {max} characters")
    @Pattern(regexp = "^(?=.*[A-Z])(?=.*[!@#$%^&*]).*$",
            message = "Password must include at least one uppercase letter and one special character")
    private String password;

    @Email(message = "Invalid email")
    @NotNull(message = "Email cannot be null")
    private String email;
}
