package com.example.msaccount.model.dto;

import lombok.*;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AccountDTO {
    private String accountName;
    private Boolean accountEnable;
    private String avatarUrl;

    private String roleName;
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private Boolean isAdmin;
}
