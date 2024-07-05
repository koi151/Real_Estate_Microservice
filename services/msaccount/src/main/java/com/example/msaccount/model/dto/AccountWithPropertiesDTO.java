package com.example.msaccount.model.dto;

import lombok.Data;

@Data
public class AccountWithPropertiesDTO {

    private String accountType;
    private Long accountId;
    private String role;
    private String accountName;
    private String avatarUrl;
}
