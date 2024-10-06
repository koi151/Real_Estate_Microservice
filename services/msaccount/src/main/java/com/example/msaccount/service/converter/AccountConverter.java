package com.example.msaccount.service.converter;

import com.example.msaccount.entity.Account;
import com.example.msaccount.entity.admin.AdminAccount;
import com.example.msaccount.entity.client.ClientAccount;
import com.example.msaccount.mapper.AccountMapper;
import com.example.msaccount.model.dto.AccountDTO;
import com.example.msaccount.model.request.admin.AccountCreateRequest;
import com.example.msaccount.repository.AccountRepository;
import com.example.msaccount.service.KeycloakUserService;
import com.example.msaccount.service.admin.impl.CloudinaryServiceImpl;
import com.example.msaccount.utils.CloudinaryUploadUtil;
import com.example.msaccount.validator.AccountValidator;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;

@Component
@RequiredArgsConstructor
public class AccountConverter {

    private final KeycloakUserService keycloakUserService;
    private final CloudinaryUploadUtil cloudinaryUploadUtil;

    private final AccountMapper accountMapper;

    @Transactional
    public Account toEntity(AccountCreateRequest request, String userId, MultipartFile avatar) {
        String avatarUrl = cloudinaryUploadUtil.avatarCloudinaryUpdate(avatar);
        Account account = accountMapper.toAccountEntity(request, userId);
        account.setAvatarUrl(avatarUrl);

        if (keycloakUserService.isAdminUser(userId)) {
            AdminAccount adminAccount = createAdminAccount(account);
            account.setAdminAccount(adminAccount);
        } else {
            ClientAccount clientAccount = createClientAccount(account);
            account.setClientAccount(clientAccount);
        }

        return account;
    }

    private AdminAccount createAdminAccount(Account account) {
        return AdminAccount.builder()
            .account(account)
            .build();
    }

    private ClientAccount createClientAccount(Account account) {
        return ClientAccount.builder()
            .account(account)
            .balance(BigDecimal.valueOf(0))
            .build();
    }



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

//    public AccountDTO toAccountDTO(AccountCreateRequest request, String avatarUrl) {
//        return AccountDTO.builder()
//            .accountName(request.getAccountType())
//            .phone(request.phone())
//            .firstName(request.firstName())
//            .lastName(request.lastName())
//            .email(request.email())
//            .avatarUrl(avatarUrl)
////            .role(account.getRole().getName())
//            .build();
//    }

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
