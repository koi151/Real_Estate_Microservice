package com.example.msaccount.model.dto;

import lombok.Data;

import java.util.List;

@Data
public class AccountWithPropertiesDTO {

    private Long accountId;
//    private String accountType;
//    private String role;
    private String accountName;
    private String avatarUrl;
    List<PropertyDTO> properties;

    private String title;
}
