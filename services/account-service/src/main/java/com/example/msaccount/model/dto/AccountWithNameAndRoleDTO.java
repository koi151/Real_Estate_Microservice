package com.example.msaccount.model.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class AccountWithNameAndRoleDTO {
    private String accountName;
    private List<String> roleNames;
}
