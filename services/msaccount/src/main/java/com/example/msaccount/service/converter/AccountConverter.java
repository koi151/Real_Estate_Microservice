package com.example.msaccount.service.converter;

import com.example.msaccount.customExceptions.CloudinaryUploadFailedException;
import com.example.msaccount.customExceptions.RoleNotFoundException;
import com.example.msaccount.enums.AccountTypeEnum;
import com.example.msaccount.model.dto.AccountDTO;
import com.example.msaccount.model.dto.AccountWithPropertiesDTO;
import com.example.msaccount.model.dto.admin.AdminAccountDTO;
import com.example.msaccount.model.request.AccountCreateRequest;
import com.example.msaccount.service.admin.impl.CloudinaryServiceImpl;
import com.example.msaccount.utils.StringUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

@Component
@RequiredArgsConstructor
public class AccountConverter {

//    private final PasswordEncoder passwordEncoder;
//
//    private final RoleRepository roleRepository;


    // using builder instead of java reflection for better performance, type safety (errors are caught at compile time rather than at runtime)
//    public Account toAccountEntity(AccountCreateRequest request, MultipartFile avatar) {
//        String hashedPassword = passwordEncoder.encode(request.getPassword());
//        String avatarUrl = avatarCloudinaryUpdate(avatar);
//        Role role = roleRepository.findById(request.getRoleId())
//                .orElseThrow(() -> new RoleNotFoundException("Role not found with id:" + request.getRoleId()));
//
//        Account account = Account.builder()
//                .role(role)
//                .phone(request.getPhone())
//                .firstName(request.getFirstName())
//                .lastName(request.getLastName())
//                .password(hashedPassword)
//                .email(request.getEmail())
//                .accountName(request.getAccountName())
//                .accountStatus(request.getStatus())
//                .avatarUrl(avatarUrl)
//                .build();
//
//        if (request.getAccountType() == AccountTypeEnum.ADMIN) {
//
//            AdminAccount adminAccount = AdminAccount.builder()
//                    .account(account)
//                    .build();
//
//            account.setAdminAccount(adminAccount);
//        } else {
//            ClientAccount clientAccount = ClientAccount.builder()
//                    .balance(0.0)
//                    .account(account)
//                    .build();
//            account.setClientAccount(clientAccount);
//        }
//
//        return account;
//    }

//    public toAccountWithPropertiesDTO toAccountWithPropertiesDTO(Account)

    public AccountDTO toAccountDTO(AccountCreateRequest request, String avatarUrl) {
        return AccountDTO.builder()
            .accountName(request.accountName())
            .phone(request.phone())
            .firstName(request.firstName())
            .lastName(request.lastName())
            .email(request.email())
            .avatarUrl(avatarUrl)
//            .role(account.getRole().getName())
            .build();
    }

//    public AdminAccountDTO toAdminAccountDTO(Account account) {
//        return AdminAccountDTO.builder()
//                .accountId(account.getAccountId())
//                .accountName(account.getAccountName())
//                .phone(account.getPhone())
//                .status(account.getAccountStatus().getStatus())
//                .firstName(account.getFirstName())
//                .lastName(account.getLastName())
//                .email(account.getEmail())
//                .avatarUrl(account.getAvatarUrl())
//                .role(account.getRole().getName())
//                .build();
//    }
}
