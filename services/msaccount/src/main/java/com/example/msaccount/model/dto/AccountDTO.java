package com.example.msaccount.model.dto;

import lombok.*;

import java.util.List;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AccountDTO {
    private String username;
    private List<String> roleNames;
    private String firstName;
    private String lastName;
    private String email;
    private Boolean accountEnable;
    private String avatarUrl;
    private String phone;
}
