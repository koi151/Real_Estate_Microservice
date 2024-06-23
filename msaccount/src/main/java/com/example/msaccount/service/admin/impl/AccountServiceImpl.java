package com.example.msaccount.service.admin.impl;

import com.example.msaccount.customExceptions.*;
import com.example.msaccount.dto.AccountDTO;
import com.example.msaccount.dto.payload.request.AccountCreateRequest;
import com.example.msaccount.dto.payload.request.AccountUpdateRequest;
import com.example.msaccount.entity.AccountEntity;
import com.example.msaccount.entity.admin.AdminAccountEntity;
import com.example.msaccount.entity.admin.AdminRoleEntity;
import com.example.msaccount.enums.AccountStatusEnum;
import com.example.msaccount.enums.AccountTypeEnum;
import com.example.msaccount.repository.admin.AccountRepository;
import com.example.msaccount.repository.admin.RoleRepository;
import com.example.msaccount.service.admin.AccountService;
import com.example.msaccount.utils.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class AccountServiceImpl implements AccountService {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    CloudinaryServiceImpl cloudinaryServiceImpl;

    @Override
    public AccountDTO createAccount(AccountCreateRequest request, MultipartFile avatar) {

        if (accountRepository.existsByPhone(request.getPhone()))
            throw new PhoneAlreadyExistsException("Phone number already exists");

        if(accountRepository.existsByUserName(request.getUserName()))
            throw new UserNameAlreadyExistsException("User name already exists");

        AccountEntity accountEntity;

        if (request.getAccountType() == AccountTypeEnum.ADMIN) {
            AdminRoleEntity role = roleRepository.findById(request.getAdminRoleId())
                    .orElseThrow(() -> new RoleNotFoundException("Role not found with id:" + request.getAdminRoleId()));

            String hashedPassword = passwordEncoder.encode(request.getPassword()); // length = 60

            AdminAccountEntity adminAccountEntity = AdminAccountEntity.builder()
                    .role(role)
                    .build();

            accountEntity = AccountEntity.builder() // DTO -> entity
                    .phone(request.getPhone())
                    .firstName(request.getFirstName())
                    .lastName(request.getLastName())
                    .password(hashedPassword)
                    .email(request.getEmail())
                    .adminAccountEntity(adminAccountEntity)
                    .userName(request.getUserName())
                    .build();
        } else {
//            String hashedPassword = passwordEncoder.encode(request.getPassword()); // length = 60



        }

        accountRepository.save(accountEntity);


        // develop: in case of current account do not have permission to create account


        if (avatar != null && !avatar.isEmpty()) {
            String avatarUploadedUrl = cloudinaryServiceImpl.uploadFile(avatar, "real_estate_account");

            if (!StringUtil.checkString(avatarUploadedUrl))
                throw new CloudinaryUploadFailedException("Failed to upload images to Cloudinary");

            newAccount.setAvatarUrl(avatarUploadedUrl);
        }

        accountRepository.save(newAccount);


//        AccountEntity savedAccountEntity = accountRepository.save(accountEntity);
//
//        return new AccountDTO(savedAccountEntity.getAccountId(), savedAccountEntity.getUserName(), savedAccountEntity.getPhone(), savedAccountEntity.getAccountStatus(),
//                              savedAccountEntity.getFirstName(), savedAccountEntity.getLastName(), savedAccountEntity.getEmail(), savedAccountEntity.getAvatarUrl());

        return null;
    }

    @Override
    public AccountDTO updateAccount(Integer id, AccountUpdateRequest request, MultipartFile avatarFile) {
        return accountRepository.findByAccountIdAndDeleted(id, false)
                .map(existingAccountEntity -> {
                    updateAccountDetails(existingAccountEntity, request);
                    updateAvatar(existingAccountEntity, avatarFile);

                    return accountRepository.save(existingAccountEntity);
                })
                .map(this::convertToAccountDTO)
                .orElseThrow(() -> new AccountNotFoundException("Cannot find account with id: " + id));
    }


    public List<AccountDTO> getAccountsByStatus(AccountStatusEnum status, Integer pageSize) {
        PageRequest pageRequest = PageRequest.of(0, pageSize, Sort.by("accountId"));
        Page<AccountEntity> accounts = accountRepository.findByAccountStatusAndDeleted(status, false, pageRequest);

        return accounts.stream()
                .map(accountEntity -> new AccountDTO(accountEntity.getAccountId(), accountEntity.getUserName(), accountEntity.getPhone(), accountEntity.getAccountStatus(),
                        accountEntity.getFirstName(), accountEntity.getLastName(), accountEntity.getEmail(), accountEntity.getAvatarUrl()))
                .collect(Collectors.toList());
    }

    @Override
    public void deleteAccount(Integer id) {
        AccountEntity accountEntity = accountRepository.findById(id)
                .orElseThrow(() -> new AccountNotFoundException("Account with id " + id + " not found"));

        accountEntity.setDeleted(true);
        accountRepository.save(accountEntity);
    }

    private void updateAvatar(AccountEntity existingAccountEntity, MultipartFile avatarFile) {
        if (avatarFile != null && !avatarFile.isEmpty()) {
            String uploadedAvatarUrl = cloudinaryServiceImpl.uploadFile(avatarFile, "real_estate_account");
            if (uploadedAvatarUrl == null || uploadedAvatarUrl.isEmpty()) {
                throw new CloudinaryUploadFailedException("Failed to upload images to Cloudinary");
            }
            existingAccountEntity.setAvatarUrl(uploadedAvatarUrl);
        }
    }

    private void updateAccountDetails(AccountEntity existingAccountEntity, AccountUpdateRequest request) {
        if (request != null) {
            if (request.getPhone() != null && accountRepository.existsByPhone(request.getPhone()))
                throw new PhoneAlreadyExistsException("Phone number already exists");

            if (request.getUserName() != null && accountRepository.existsByUserName(request.getUserName()))
                throw new UserNameAlreadyExistsException("User name already exists");

            // Use Optional for Null check
            Optional.ofNullable(request.getUserName()).ifPresent(existingAccountEntity::setUserName);
            Optional.ofNullable(request.getFirstName()).ifPresent(existingAccountEntity::setFirstName);
            Optional.ofNullable(request.getLastName()).ifPresent(existingAccountEntity::setLastName);
            Optional.ofNullable(request.getPhone()).ifPresent(existingAccountEntity::setPhone);
            Optional.ofNullable(request.getEmail()).ifPresent(existingAccountEntity::setEmail);
            Optional.ofNullable(request.getStatus()).ifPresent(existingAccountEntity::setAccountStatus);
            Optional.ofNullable(request.getPassword())
                    .map(passwordEncoder::encode)
                    .ifPresent(existingAccountEntity::setPassword);

            if (request.getAvatarUrlRemove()) {
                existingAccountEntity.setAvatarUrl(null);
            }
        }
    }

    private AccountDTO convertToAccountDTO(AccountEntity savedAccountEntity) {
        return AccountDTO.builder()
                .accountId(savedAccountEntity.getAccountId())
                .userName(savedAccountEntity.getUserName())
                .firstName(savedAccountEntity.getFirstName())
                .lastName(savedAccountEntity.getLastName())
                .email(savedAccountEntity.getEmail())
                .phone(savedAccountEntity.getPhone())
                .accountStatus(savedAccountEntity.getAccountStatus())
                .avatarUrl(savedAccountEntity.getAvatarUrl())
                .build();
    }

}
