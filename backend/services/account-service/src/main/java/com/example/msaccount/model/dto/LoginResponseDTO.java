package com.example.msaccount.model.dto;

import lombok.*;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class LoginResponseDTO {
    private AccountDetailDTO accountInfo;
    private TokenResponseDTO tokenResponses;
}
