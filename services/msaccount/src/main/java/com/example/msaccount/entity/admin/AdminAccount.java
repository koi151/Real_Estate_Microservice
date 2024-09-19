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
    private Long accountId;

    @OneToOne
    @MapsId
    @JoinColumn(name = "account_id")
    private Account account;
}


