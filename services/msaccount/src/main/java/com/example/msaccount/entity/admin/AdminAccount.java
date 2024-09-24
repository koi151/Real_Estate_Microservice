package com.example.msaccount.entity.admin;

import com.example.msaccount.entity.Account;
import jakarta.persistence.*;
import lombok.*;

@Entity(name = "admin_account")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AdminAccount {

    @Id
    @Column(name = "account_id", columnDefinition = "VARCHAR(36)", nullable = false)
    private String accountId;

    @OneToOne
    @MapsId
    @JoinColumn(name = "account_id")
    private Account account;
}


