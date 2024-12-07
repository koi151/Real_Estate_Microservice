package com.example.msaccount.model.dto.admin;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Setter
@Getter
@Builder
public class AdminAccountDTO {
    private String accountId;
    private String accountName;
    private String phone;
    private String status;
    private String firstName;
    private String lastName;
    private String email;
    private String avatarUrl;
    private List<String> roles;
    private LocalDateTime createdAt;
}
