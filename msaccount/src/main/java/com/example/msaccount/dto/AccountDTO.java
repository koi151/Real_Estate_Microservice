package com.example.msaccount.dto;

import com.example.msaccount.entity.AccountStatusEnum;
import lombok.*;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AccountDTO {

    private int accountId;
    private String userName;
    private String phone;
    private AccountStatusEnum accountStatus;
    private String firstName;
    private String lastName;
    private String email;
    private String avatarUrl;
}