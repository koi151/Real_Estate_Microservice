package com.example.msaccount.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class TokenResponseDTO {
    private String accessToken;
    private String refreshToken;
    private int expiresIn;
}
