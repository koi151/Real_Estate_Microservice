package com.example.msaccount.model.dto;

import com.example.msaccount.enums.AccountStatusEnum;
import lombok.*;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AccountDTO {

    private String accountType;
//    private String role;
    private String accountName;
    private String phone;
    private String accountStatus;
    private String firstName;
    private String lastName;
    private String email;
    private String avatarUrl;
}
