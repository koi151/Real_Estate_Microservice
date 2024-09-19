package com.example.msaccount.entity.client;

import com.example.msaccount.entity.Account;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.*;

import java.util.UUID;

@Entity(name = "client_account")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ClientAccount {

    @Id
    private UUID accountId;

    @OneToOne
    @MapsId
    @JoinColumn(name = "account_id")
    private Account account;

    @PositiveOrZero(message = "Balance must be non-negative value")
    @NotNull(message = "Account balance cannot be null")
    private Double balance;
}
