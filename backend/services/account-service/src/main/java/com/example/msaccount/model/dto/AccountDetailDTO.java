package com.example.msaccount.model.dto;

import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.Set;

@SuperBuilder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AccountDetailDTO {
    private String accountId;
    private Boolean isAdmin;
    private String username;
    private Set<String> roleNames;
    private Set<String> scopes;
    private String firstName;
    private String lastName;
    private String email;
    private Boolean accountEnabled = true;
    private String avatarUrl;
    private String phone;
    private Boolean emailVerified;
}
