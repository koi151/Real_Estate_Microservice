package com.example.msaccount.model.dto;

import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
public class ClientDatabaseAccountDTO extends CommonDatabaseAccountDTO {
    private BigDecimal balance;

    // Explicit constructor that matches the JPQL query requirements
    public ClientDatabaseAccountDTO(String phone, String avatarUrl, BigDecimal balance) {
        super(phone, avatarUrl);
        this.balance = balance;
    }
}
