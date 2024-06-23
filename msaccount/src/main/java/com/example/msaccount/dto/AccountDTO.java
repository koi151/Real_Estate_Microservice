package com.example.msaccount.dto;

import com.example.msaccount.enums.AccountStatusEnum;
import lombok.*;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AccountDTO {

    private Long accountId;
    private String userName;
    private String phone;
    private AccountStatusEnum accountStatus;
    private String firstName;
    private String lastName;
    private String email;
    private String avatarUrl;
}
