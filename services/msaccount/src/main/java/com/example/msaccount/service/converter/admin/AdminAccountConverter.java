package com.example.msaccount.service.converter.admin;

import com.example.msaccount.entity.admin.AdminAccount;
import com.example.msaccount.model.dto.AccountSearchDTO;
import com.example.msaccount.model.dto.admin.AdminAccountDTO;
import org.springframework.stereotype.Component;

@Component
public class AdminAccountConverter {

    public AdminAccountDTO toAdminAccountDTO(AdminAccount adminAccount) {
        var account = adminAccount.getAccount();
        return AdminAccountDTO.builder()
                .accountId(adminAccount.getAccountId())
                .accountName(account.getAccountName())
                .phone(account.getPhone())
                .status(account.getAccountStatus().getStatus())
                .firstName(account.getFirstName())
                .lastName(account.getLastName())
                .email(account.getEmail())
                .avatarUrl(account.getAvatarUrl())
                .role(account.getRole().getName())
                .build();
    }

    public AccountSearchDTO toAccountSearchDTO(AdminAccount adminAccount) {
        var account = adminAccount.getAccount();
        return AccountSearchDTO.builder()
                .accountId(account.getAccountId())
                .accountName(account.getAccountName())
                .phone(account.getPhone())
                .status(account.getAccountStatus())
                .firstName(account.getFirstName())
                .lastName(account.getLastName())
                .email(account.getEmail())
                .avatarUrl(account.getAvatarUrl())
                .build();
    }
}
