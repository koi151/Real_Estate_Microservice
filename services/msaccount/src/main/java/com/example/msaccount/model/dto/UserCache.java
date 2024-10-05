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

    private String userId;
    private String email;
    private Set<String> scopes;
}