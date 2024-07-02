package com.example.msaccount.service.converter.admin;

import com.example.msaccount.entity.admin.AdminAccount;
import com.example.msaccount.model.dto.admin.AdminAccountDTO;
import org.springframework.stereotype.Component;

@Component
public class AdminAccountConverter {

    public AdminAccountDTO toAdminAccountDTO(AdminAccount account) {
        var adminAccount = account.getAccount();
        return AdminAccountDTO.builder()
                .accountId(account.getAccountId())
                .accountName(adminAccount.getAccountName())
                .phone(adminAccount.getPhone())
                .status(adminAccount.getAccountStatus().getStatus())
                .firstName(adminAccount.getFirstName())
                .lastName(adminAccount.getLastName())
                .email(adminAccount.getEmail())
                .avatarUrl(adminAccount.getAvatarUrl())
                .role(adminAccount.getRole().getName())
                .build();
    }
}
