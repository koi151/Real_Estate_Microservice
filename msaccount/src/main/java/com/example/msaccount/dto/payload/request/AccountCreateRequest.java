package com.example.msaccount.dto.payload.request;

import com.example.msaccount.enums.AccountStatusEnum;
import com.example.msaccount.enums.AccountTypeEnum;
import com.example.msaccount.utils.StringUtil;
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

    @NotBlank(message = "Account type cannot be blank")
    @Enumerated(EnumType.STRING)
    private AccountTypeEnum accountType;

    private Long adminRoleId;

    @NotBlank(message = "User name cannot be blank")
    @Pattern(regexp = "[A-Za-z0-9.\\s]+", message = "Username contains invalid characters")
    private String userName;

    @NotNull(message = "Phone number cannot be null")
    private String phone;

    @Enumerated(EnumType.STRING)
    private AccountStatusEnum status = AccountStatusEnum.ACTIVE;

    @Size(min = 1, max = 30, message = "First name length must be between {min} and {max} characters")
    @Pattern(regexp = "[A-Za-z.\\s]+", message = "First name contains invalid characters")
    private String firstName;

    @Size(min = 1, max = 70, message = "Last name length must be between {min} and {max} characters")
    @Pattern(regexp = "[A-Za-z.\\s]+", message = "Last name contains invalid characters")
    private String lastName;

    @NotBlank(message = "Password cannot be blank")
    @Size(min = 6, max = 100, message = "Password length must be between {min} and {max} characters")
    @Pattern(regexp = "^(?=.*[A-Z])(?=.*[!@#$%^&*]).*$",
            message = "Password must include at least one uppercase letter and one special character")
    private String password;

    @Email(message = "Invalid email")
    @NotBlank(message = "Email cannot be blank")
    private String email;

    @Column(name = "facebook_account_id")
    private int facebookAccountId;

    @Column(name = "google_account_id")
    private int googleAccountId;

    @AssertTrue(message = "Password retype does not match")
    private boolean isPasswordMatch(String password) {
        return StringUtil.checkString(password) && password.equals(retypePassword);
    }
}
