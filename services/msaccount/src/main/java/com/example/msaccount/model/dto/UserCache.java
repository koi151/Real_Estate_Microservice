package com.example.msaccount.model.dto;

import lombok.*;

import java.io.Serial;
import java.io.Serializable;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserCache implements Serializable {
    @Serial
    private static final long serialVersionUID = 4514816687469018078L;

    private String accountId;
    private String username;
    private Boolean isAdmin;
    private String phone;
    private String firstName;
    private String lastName;
    private String email;
    private Set<String> roleNames;
    private Set<String> scopes;
    private Boolean emailVerified;
}