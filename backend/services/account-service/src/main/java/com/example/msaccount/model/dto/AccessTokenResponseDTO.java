package com.example.msaccount.model.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AccessTokenResponseDTO {
    private String accessToken;
    private int expiresIn;
}