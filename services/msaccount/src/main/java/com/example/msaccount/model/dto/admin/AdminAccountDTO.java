package com.example.msaccount.model.dto.admin;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Setter
@Getter
@Builder
public class AdminAccountDTO {

    private Long accountId;
    private String accountName;
    private String phone;
    private String status;
    private String firstName;
    private String lastName;
    private String email;
    private String avatarUrl;
    private String role;
    private LocalDateTime createdAt;
}
