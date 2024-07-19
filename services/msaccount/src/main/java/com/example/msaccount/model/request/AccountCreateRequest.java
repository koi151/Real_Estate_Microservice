package com.example.msaccount.model.request;

import com.example.msaccount.enums.AccountStatusEnum;
import com.example.msaccount.enums.AccountTypeEnum;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
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
    private Long accountId;

    @Enumerated(EnumType.STRING)
    private AccountTypeEnum accountType = AccountTypeEnum.CLIENT;

    private Long roleId;

    @NotBlank(message = "User name is mandatory")
    @Size(min = 5, max = 30, message = "User name length must be between {min} and {max} characters")
    @Pattern(regexp = "[A-Za-z0-9.\\s]+", message = "Account name contains invalid characters")
    private String accountName;

    @NotBlank(message = "Phone number is mandatory")
    private String phone;

    @Enumerated(EnumType.STRING)
    private AccountStatusEnum status = AccountStatusEnum.ACTIVE;

    @NotBlank(message = "First name is mandatory")
    @Size(min = 1, max = 30, message = "First name length must be between {min} and {max} characters")
    @Pattern(regexp = "[A-Za-z.\\s]+", message = "First name contains invalid characters")
    private String firstName;

    @NotBlank(message = "Last name is mandatory")
    @Size(min = 1, max = 70, message = "Last name length must be between {min} and {max} characters")
    @Pattern(regexp = "[A-Za-z.\\s]+", message = "Last name contains invalid characters")
    private String lastName;

    @NotBlank(message = "Password cannot be blank")
    @Size(min = 6, max = 100, message = "Password length must be between {min} and {max} characters")
    @Pattern(regexp = "^(?=.*[A-Z])(?=.*[!@#$%^&*]).*$",
            message = "Password must include at least one uppercase letter and one special character")
    private String password;

    @NotBlank(message = "Password retype is mandatory")
    private String retypePassword;

    @Email(message = "Invalid email")
    @NotBlank(message = "Email cannot be blank")
    private String email;

    @Column(name = "facebook_account_id")
    private long facebookAccountId;

    @Column(name = "google_account_id")
    private long googleAccountId;
}
