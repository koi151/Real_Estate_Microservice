package com.example.msaccount.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class AccountSearchDTO {
    private Long accountId;
    private String accountName;
    private String phone;
//    private AccountStatusEnum status;
    private boolean accountStatus;
    private String firstName;
    private String lastName;
    private String email;
    private String avatarUrl;
}
