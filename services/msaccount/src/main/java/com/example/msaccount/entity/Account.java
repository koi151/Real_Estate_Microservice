package com.example.msaccount.entity;

import com.example.msaccount.entity.admin.AdminAccount;
import com.example.msaccount.entity.client.ClientAccount;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

import java.io.Serial;

@Entity(name = "account")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Account extends BaseEntity {

    @Serial
    private static final long serialVersionUID = -896035267760632084L;

    @Id
    @Column(name = "account_id", columnDefinition = "VARCHAR(36)", nullable = false)
    private String accountId;

    @OneToOne(mappedBy = "account", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, orphanRemoval = true)
    private AdminAccount adminAccount;

    @OneToOne(mappedBy = "account", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, orphanRemoval = true)
    private ClientAccount clientAccount;

    @Column(name = "phone", length = 20, nullable = false)
    @NotBlank(message = "Phone number is mandatory")
    private String phone;

//    @Column(name = "status", length = 20, nullable = false)
//    @Enumerated(EnumType.STRING)
//    private AccountStatusEnum accountEnable;

    @Column(name = "enable", nullable = false)
    private boolean accountEnable;

    @Column(name = "avatar_url")
    private String avatarUrl;

    @Column(name = "facebook_account_id")
    private int facebookAccountId;

    @Column(name = "google_account_id")
    private int googleAccountId;
}

