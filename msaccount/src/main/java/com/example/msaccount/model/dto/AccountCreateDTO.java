package com.example.msaccount.model.dto;

import com.example.msaccount.enums.AccountStatusEnum;
import lombok.*;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AccountCreateDTO {

    private String accountType;
    private Long accountId;
    private String role;
    private String accountName;
    private String phone;
    private AccountStatusEnum accountStatus;
    private String firstName;
    private String lastName;
    private String email;
    private String avatarUrl;
}
