package com.example.msaccount.model.dto;

import lombok.Data;

import java.util.List;

@Data
public class AccountWithPropertiesDTO {
    private String accountId;
    private String accountName;
    private String avatarUrl;
    List<PropertyDTO> properties;
}
