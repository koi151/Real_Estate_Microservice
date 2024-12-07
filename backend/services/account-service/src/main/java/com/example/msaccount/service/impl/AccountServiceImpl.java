package com.example.msaccount.service.impl;

import com.example.msaccount.customExceptions.CloudinaryUploadFailedException;
import com.example.msaccount.customExceptions.EntityNotFoundException;
import com.example.msaccount.customExceptions.KeycloakResourceNotFoundException;
import com.example.msaccount.customExceptions.RedisDataNotFound;
import com.example.msaccount.entity.Account;
import com.example.msaccount.mapper.AccountMapper;
import com.example.msaccount.model.dto.AccountDetailDTO;
import com.example.msaccount.model.dto.AccountWithNameAndRoleDTO;
import com.example.msaccount.model.dto.KeycloakUserDTO;
import com.example.msaccount.model.request.admin.AccountCreateRequest;
import com.example.msaccount.model.request.admin.AccountUpdateRequest;
import com.example.msaccount.repository.AccountRepository;
import com.example.msaccount.service.KeycloakUserService;
import com.example.msaccount.service.AccountService;
import com.example.msaccount.service.converter.AccountConverter;
import com.example.msaccount.validator.AccountValidator;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
@Slf4j
public class AccountServiceImpl implements AccountService {

//    private final PasswordEncoder passwordEncoder;
//    private final AccountRepository accountRepository;
//    private final AdminAccountRepository adminAccountRepository;
    private final CloudinaryServiceImpl cloudinaryServiceImpl;
    private final AccountConverter accountConverter;

//    private final PropertiesClient propertiesClient;

    private final AccountRepository accountRepository;
    private final AccountValidator accountValidator;
    private final KeycloakUserService keycloakUserService;
    private final AccountMapper accountMapper;
    private final AuthServiceImpl authServiceImpl;

    private final RedisTemplate<String, Object> redisTemplate;
    private final ObjectMapper objectMapper;

//    @Override
//    public List<AdminAccountDTO> findAllAdminAccounts() {
//        return adminAccountRepository.findAllByAccountDeleted(false, Sort.by("accountId")).stream()
//            .map(adminAccountConverter::toAdminAccountDTO)
//            .collect(Collectors.toList());
//    }

//    @Override
//    public AccountDTO findAccountDetails(Long accountId) {
//        return accountMapper.toAccountDTO(
//            accountRepository.findByAccountIdAndAccountStatusAndDeleted(accountId, AccountStatusEnum.ACTIVE, false)
//                .orElseThrow(() -> new AccountNotFoundException("Account not found with id: " + accountId))
//        );
//    }

    @Override
    @Transactional
    public AccountDetailDTO updateCurrentAccount(String authorizationHeader, AccountUpdateRequest request, MultipartFile avatarFile) {
        accountValidator.validateAccountUpdateRequest(request, avatarFile);;

        String accountId = authServiceImpl.extractUserIDFromAuthHeader(authorizationHeader);

        KeycloakUserDTO kcDTO =  keycloakUserService.updateAccount(request, accountId); // keycloak update
        return accountRepository.findByAccountIdAndAccountEnableAndDeleted(accountId, true, false)
            .map(existingEntity -> {
                accountMapper.updateAccountFromRequest(request, existingEntity); // db update
                return accountRepository.save(existingEntity);
            })
            .map(entity -> accountMapper.entityToAccountDTO(entity, kcDTO))
            .orElseThrow(() -> new EntityNotFoundException("Account not found with id: " + accountId));
    }


    @Override
    public AccountWithNameAndRoleDTO findAccountNameAndRoleById(String uuid) {
        var kc = keycloakUserService.retrieveRoleNamesById(uuid);
        return accountRepository.findByAccountIdAndAccountEnableAndDeleted(uuid, true, false)
            .map(accountMapper::toAccountWithNameAndRoleDTO)
            .orElseThrow(() -> new KeycloakResourceNotFoundException("Account not found with id:" + uuid));
    }


    @Override
    @Transactional
    public AccountDetailDTO createAccount(AccountCreateRequest request, MultipartFile avatar)  {
        accountValidator.validateAccountCreateRequest(request);

        KeycloakUserDTO kcUserDTO = keycloakUserService.createUser(request);

        Account account = accountConverter.toEntity(request, kcUserDTO.id(), avatar);
        accountRepository.save(account);

        return accountMapper.requestToAccountDTO(account, kcUserDTO);
    }

    @Override
    public AccountDetailDTO findAccountDetails(String uuid) {
        Account existedAcc = accountRepository.findById(uuid)
            .orElseThrow(() -> new EntityNotFoundException("Account cannot be found with id: " + uuid));

        KeycloakUserDTO keycloakUser = keycloakUserService.fetchUserDetailsFromKeycloak(existedAcc.getAccountId());

        return AccountDetailDTO.builder()
            .accountId(existedAcc.getAccountId())
            .phone(existedAcc.getPhone())
            .avatarUrl(existedAcc.getAvatarUrl())
            .accountEnabled(existedAcc.isAccountEnable())
            .firstName(keycloakUser.firstName())
            .lastName(keycloakUser.lastName())
            .email(keycloakUser.email())
            .roleNames(keycloakUser.roleNames())
            .accountType(existedAcc.getAdminAccount() != null ? "Admin" : "Client")
            .balance(existedAcc.getClientAccount() != null ? existedAcc.getClientAccount().getBalance() : null)
            .createdDate(existedAcc.getAdminAccount() != null
                ? existedAcc.getAdminAccount().getCreatedDate()
                : existedAcc.getClientAccount().getCreatedDate())
            .build();
    }


    public AccountDetailDTO getAccountDataFromHeader(String authorizationHeader) {
        String userId = AuthServiceImpl.extractUserIdFromToken(authorizationHeader);
        String key = "userId:" + userId;
        try {
            Object redisData = redisTemplate.opsForValue().get(key);
            return objectMapper.convertValue(redisData, AccountDetailDTO.class);

        } catch (ClassCastException ex) {
            throw new RedisDataNotFound("Data not exists or incorrect key");
        }
    }

    @Override
    public AccountDetailDTO getCurrentAccountInfo(String authorizationHeader) {
        String accessToken = authServiceImpl.extractAccessTokenFromAuthHeader(authorizationHeader);
        AccountDetailDTO accountDTO = authServiceImpl.extractUserInfoFromAccessToken(accessToken);
        String accountId = accountDTO.getAccountId();

        return accountDTO.getIsAdmin()
            ? accountRepository.findAdminAccountInfoFromDB(accountId)
            .map(dbDto -> accountMapper.mapAdminDatabaseAccountDTO(dbDto, accountDTO))
            .orElseThrow(() -> new EntityNotFoundException("Cannot found admin account information in database with id: " + accountId))

            : accountRepository.findClientAccountInfoFromDB(accountId)
            .map(dbDto -> accountMapper.mapClientDatabaseAccountDTO(dbDto, accountDTO))
            .orElseThrow(() -> new EntityNotFoundException("Cannot found client account information in database with id: " + accountId));
    }


    private void updateAvatar(Account existingAccount, MultipartFile avatarFile) {
        if (avatarFile != null && !avatarFile.isEmpty()) {
            String uploadedAvatarUrl = cloudinaryServiceImpl.uploadFile(avatarFile, "real_estate_account");
            if (uploadedAvatarUrl == null || uploadedAvatarUrl.isEmpty()) {
                throw new CloudinaryUploadFailedException("Failed to upload images to Cloudinary");
            }
            existingAccount.setAvatarUrl(uploadedAvatarUrl);
        }
    }

//    public List<AccountSearchDTO> getAccountsByStatus(AccountStatusEnum status, Integer pageSize) {
////        PageRequest pageRequest = PageRequest.of(0, pageSize, Sort.by("accountId")); tempo cmt
//        List<AdminAccount> accounts = adminAccountRepository.findByAccountStatusDeleted(status, false, Sort.by("accountId"));
//
//        return accounts.stream()
//                .map(adminAccountConverter::toAccountSearchDTO)
//                .collect(Collectors.toList());
//    }

//    @Override
//    public void deleteAdminAccount(Long id) {
//        Account account = accountRepository.findById(id)
//                .orElseThrow(() -> new AccountNotFoundException("Admin account with id " + id + " not found"));
//
//        account.setDeleted(true);
//        accountRepository.save(account);
//    }


//    private void updateAccountDetails(Account existingAccount, AccountUpdateRequest request) {
//        if (request != null) {
//            if (request.getPhone() != null && accountRepository.existsByPhone(request.getPhone()))
//                throw new PhoneAlreadyExistsException("Phone number already exists");
//
//            if (request.getAccountName() != null && accountRepository.existsByAccountName(request.getAccountName()))
//                throw new AccountAlreadyExistsException("Account name already exists");
//
//            // Use Optional for Null check
//            Optional.ofNullable(request.getAccountName()).ifPresent(existingAccount::setAccountName);
//            Optional.ofNullable(request.getFirstName()).ifPresent(existingAccount::setFirstName);
//            Optional.ofNullable(request.getLastName()).ifPresent(existingAccount::setLastName);
//            Optional.ofNullable(request.getPhone()).ifPresent(existingAccount::setPhone);
//            Optional.ofNullable(request.getEmail()).ifPresent(existingAccount::setEmail);
//            Optional.ofNullable(request.getStatus()).ifPresent(existingAccount::setAccountStatus);
//            Optional.ofNullable(request.getPassword())
//                    .map(passwordEncoder::encode)
//                    .ifPresent(existingAccount::setPassword);
//
//            if (request.getAvatarUrlRemove()) {
//                existingAccount.setAvatarUrl(null);
//            }
//        }
//    }

//    private AccountDTO convertToAccountDTO(Account savedAccount) {
//        return AccountDTO.builder()
//            .accountName(savedAccount.getAccountName())
//            .firstName(savedAccount.getFirstName())
//            .lastName(savedAccount.getLastName())
//            .email(savedAccount.getEmail())
//            .phone(savedAccount.getPhone())
//            .accountStatus(savedAccount.getAccountStatus().getStatus())
//            .avatarUrl(savedAccount.getAvatarUrl())
//            .build();
//    }
}
