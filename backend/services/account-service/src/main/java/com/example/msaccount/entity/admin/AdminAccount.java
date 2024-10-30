package com.example.msaccount.entity.admin;

import com.example.msaccount.entity.Account;
import com.example.msaccount.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.io.Serial;

@Entity(name = "admin_account")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@NamedEntityGraph(name = "AdminAccount.detail", attributeNodes = @NamedAttributeNode("account"))
public class AdminAccount extends BaseEntity {

    @Serial
    private static final long serialVersionUID = -6707294041383577522L;

    @Id
    @Column(name = "account_id", columnDefinition = "VARCHAR(36)", nullable = false)
    private String accountId;

    @OneToOne
    @MapsId
    @JoinColumn(name = "account_id")
    private Account account;
}


