package com.example.msaccount.service.converter;

import com.example.msaccount.customExceptions.CloudinaryUploadFailedException;
import com.example.msaccount.customExceptions.RoleNotFoundException;
import com.example.msaccount.entity.Account;
import com.example.msaccount.entity.admin.AdminAccount;
import com.example.msaccount.entity.Role;
import com.example.msaccount.entity.client.ClientAccount;
import com.example.msaccount.enums.AccountTypeEnum;
import com.example.msaccount.model.dto.AccountDTO;
import com.example.msaccount.model.dto.admin.AdminAccountDTO;
import com.example.msaccount.model.request.AccountCreateRequest;
import com.example.msaccount.repository.admin.RoleRepository;
import com.example.msaccount.service.admin.impl.CloudinaryServiceImpl;
import com.example.msaccount.utils.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

@Component
public class AccountConverter {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private CloudinaryServiceImpl cloudinaryServiceImpl;

    public String avatarCloudinaryUpdate(MultipartFile avatar) {
        if (avatar != null && !avatar.isEmpty()) {
            String avatarUploadedUrl = cloudinaryServiceImpl.uploadFile(avatar, "real_estate_account");

            if (!StringUtil.checkString(avatarUploadedUrl))
                throw new CloudinaryUploadFailedException("Failed to upload images to Cloudinary");

            return avatarUploadedUrl;
        }
        return null;
    }

    // using builder instead of java reflection for better performance, type safety (errors are caught at compile time rather than at runtime)
    public Account toAccountEntity(AccountCreateRequest request, MultipartFile avatar) {
        String hashedPassword = passwordEncoder.encode(request.getPassword());
        String avatarUrl = avatarCloudinaryUpdate(avatar);
        Role role = roleRepository.findById(request.getRoleId())
                .orElseThrow(() -> new RoleNotFoundException("Role not found with id:" + request.getRoleId()));

        Account account = Account.builder()
                .role(role)
                .phone(request.getPhone())
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .password(hashedPassword)
                .email(request.getEmail())
                .accountName(request.getAccountName())
                .accountStatus(request.getStatus())
                .avatarUrl(avatarUrl)
                .build();

        if (request.getAccountType() == AccountTypeEnum.ADMIN) {

            AdminAccount adminAccount = AdminAccount.builder()
                    .account(account)
                    .build();

            account.setAdminAccount(adminAccount);
        } else {
            ClientAccount clientAccount = ClientAccount.builder()
                    .balance(0.0)
                    .account(account)
                    .build();
            account.setClientAccount(clientAccount);
        }

        return account;
    }


    public AccountDTO toAccountDTO(Account account) {
        return AccountDTO.builder()
                .accountId(account.getAccountId())
                .accountName(account.getAccountName())
                .phone(account.getPhone())
                .accountStatus(account.getAccountStatus())
                .firstName(account.getFirstName())
                .lastName(account.getLastName())
                .email(account.getEmail())
                .avatarUrl(account.getAvatarUrl())
                .accountType(account.getAdminAccount() != null
                        ? AccountTypeEnum.ADMIN.getAccountType()
                        : AccountTypeEnum.CLIENT.getAccountType())
                .role(account.getRole().getName())
                .build();
    }

    public AdminAccountDTO toAdminAccountDTO(Account account) {
        return AdminAccountDTO.builder()
                .accountId(account.getAccountId())
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
}
