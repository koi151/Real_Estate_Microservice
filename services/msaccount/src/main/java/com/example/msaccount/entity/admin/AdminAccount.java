package com.example.msaccount.entity.admin;

import com.example.msaccount.entity.Account;
import com.example.msaccount.entity.Role;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Entity(name = "admin_account")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AdminAccount {

    @Id
    private Long account_id;

    @OneToOne
    @MapsId
    @JoinColumn(name = "account_id")
    private Account account;
}


