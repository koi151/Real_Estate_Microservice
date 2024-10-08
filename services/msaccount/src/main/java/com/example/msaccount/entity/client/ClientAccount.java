package com.example.msaccount.entity.client;

import com.example.msaccount.entity.Account;
import com.example.msaccount.entity.BaseEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.*;

import java.io.Serial;
import java.math.BigDecimal;
import java.util.UUID;

@Entity(name = "client_account")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ClientAccount extends BaseEntity {

    @Serial
    private static final long serialVersionUID = 510311925625241455L;

    @Id
    @Column(name = "account_id", columnDefinition = "VARCHAR(36)", nullable = false)
    private String accountId;

    @OneToOne
    @MapsId
    @JoinColumn(name = "account_id")
    private Account account;

    @PositiveOrZero(message = "Balance must be non-negative value")
    @NotNull(message = "Account balance cannot be null")
    private BigDecimal balance;
}
