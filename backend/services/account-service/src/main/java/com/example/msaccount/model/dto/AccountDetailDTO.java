package com.example.msaccount.model.dto;

import lombok.*;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@SuperBuilder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AccountDetailDTO {
    private String accountId;
    private Boolean isAdmin;
    private String username;
    private List<String> roleNames;
    private Set<String> scopes;
    private String firstName;
    private String lastName;
    private String email;
    private Boolean accountEnabled = true;
    private String avatarUrl;
    private String phone;
    private Boolean emailVerified;
    private String accountType;
    private LocalDateTime createdDate;
    private BigDecimal balance; // ClientAccount
}
