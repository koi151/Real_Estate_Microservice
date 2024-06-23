package com.example.msaccount.entity.admin;

import com.example.msaccount.entity.AccountEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity(name = "admin_account")
@Getter
@Setter
public class AdminAccountEntity {

    @Id
    private int account_id;

    @OneToOne
    @MapsId
    private AccountEntity accountEntity;

    @ManyToOne
    @JoinColumn(name = "role_id")
    private RoleEntity role;
}


