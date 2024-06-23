package com.example.msaccount.model.dto;

import com.example.msaccount.enums.AccountStatusEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class AccountSearchDTO {
    private Long accountId;
    private String userName;
    private String phone;
    private AccountStatusEnum accountStatus;
    private String firstName;
    private String lastName;
    private String email;
    private String avatarUrl;
}
