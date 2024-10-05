package com.example.msaccount.service.admin.impl;

import com.example.msaccount.customExceptions.CloudinaryUploadFailedException;
import com.example.msaccount.customExceptions.EntityNotFoundException;
import com.example.msaccount.customExceptions.KeycloakResourceNotFoundException;
import com.example.msaccount.entity.Account;
import com.example.msaccount.entity.admin.AdminAccount;
import com.example.msaccount.entity.client.ClientAccount;
import com.example.msaccount.mapper.AccountMapper;
import com.example.msaccount.model.dto.*;
import com.example.msaccount.model.request.LoginRequest;
import com.example.msaccount.model.request.admin.AccountUpdateRequest;
import com.example.msaccount.model.request.admin.AccountCreateRequest;
import com.example.msaccount.repository.AccountRepository;

import com.example.msaccount.service.KeycloakUserService;
import com.example.msaccount.service.admin.AccountService;
import com.example.msaccount.service.converter.AccountConverter;
import com.example.msaccount.utils.CloudinaryUploadUtil;
import com.example.msaccount.validator.AccountValidator;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.keycloak.admin.client.CreatedResponseUtil;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.representations.AccessTokenResponse;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;

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
    public AccountWithNameAndRoleDTO findAccountNameAndRoleById(String uuid) {
        var kc = keycloakUserService.retrieveRoleNamesById(uuid);
        return accountRepository.findByAccountIdAndAccountEnableAndDeleted(uuid, true, false)
            .map(accountMapper::toAccountWithNameAndRoleDTO)
            .orElseThrow(() -> new KeycloakResourceNotFoundException("Account not found with id:" + uuid));
    }

//    @Override
//    public Page<AccountWithPropertiesDTO> findAccountWithProperties(Long accountId, Pageable pageable) {
//
//        Account account = accountRepository.findByAccountIdAndAccountStatusAndDeleted(accountId, AccountStatusEnum.ACTIVE, false)
//                .orElseThrow(() -> new AccountNotFoundException("Account not found with id: " + accountId));
//
//        // Fetch Properties using Feign Client
//        ResponseEntity<ResponseData> responseData = propertiesClient.findAllPropertiesByAccount(accountId, pageable.getPageNumber(), pageable.getPageSize());
//
//        if (responseData.getStatusCode().is2xxSuccessful() && responseData.getBody() != null) {
//            try {
//                ResponseData responseBody = responseData.getBody();
//                Object dataObject = responseBody.getData();
//
//                if (dataObject instanceof List<?> dataList) {
//                    // Convert to List<PropertyDTO> using ObjectMapper
//                    List<PropertyDTO> properties = dataList.stream()
//                            .map(item -> objectMapper.convertValue(item, PropertyDTO.class))
//                            .toList();
//
//                    AccountWithPropertiesDTO accountWithPropertiesDTO = accountMapper.toAccountWithPropertiesDTO(account, properties);
//
//                    // Wrap the result in a Page object
//                    return new PageImpl<>(List.of(accountWithPropertiesDTO), pageable, responseBody.getTotalItems()); // total properties count
//                } else {
//                    throw new RuntimeException("Unexpected data format (not a list) received from properties service");
//                }
//            } catch (Exception ex) {
//                throw new RuntimeException("Error parsing property data: " + ex.getMessage(), ex);
//            }
//        } else {
//            throw new RuntimeException("Failed to fetch properties from properties service");
//        }
//    }


    @Override
    @Transactional
    public AccountDTO createAccount(AccountCreateRequest request, MultipartFile avatar)  {
        accountValidator.validateAccountCreateRequest(request);

        KeycloakUserDTO kcUserDTO = keycloakUserService.createUser(request);

        Account account = accountConverter.toEntity(request, kcUserDTO.id(), avatar);
        accountRepository.save(account);

        return accountMapper.requestToAccountDTO(account, kcUserDTO);
    }

//    @Override
//    @Transactional
//    public AccountDTO updateAccount(AccountUpdateRequest request, MultipartFile avatarFile) {
//        KeycloakUserDTO kcDTO =  keycloakUserService.updateUser(request);
//        return accountRepository.findByAccountIdAndAccountEnableAndDeleted(request.accountId(), true, false)
//            .map(existingEntity -> {
//                accountMapper.updateAccountFromRequest(request, existingEntity);
//                return accountRepository.save(existingEntity);
//            })
//            .map(entity -> accountMapper.entityToAccountDTO(entity, kcDTO))
//            .orElseThrow(() -> new EntityNotFoundException("Account not found with id: " + request.accountId()));
//    }

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
