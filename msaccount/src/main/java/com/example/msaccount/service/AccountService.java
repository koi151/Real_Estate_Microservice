package com.example.msaccount.service;

import com.example.msaccount.dto.AccountDTO;
import com.example.msaccount.dto.payload.request.AccountCreateRequest;
import com.example.msaccount.dto.payload.request.AccountUpdateRequest;
import com.example.msaccount.entity.Account;
import com.example.msaccount.entity.AccountStatusEnum;
import com.example.msaccount.repository.AccountRepository;
import com.example.msaccount.service.imp.AccountServiceImp;
import customExceptions.AccountNotFoundException;
import customExceptions.CloudinaryUploadFailedException;
import customExceptions.PhoneAlreadyExistsException;
import customExceptions.UserNameAlreadyExistsException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class AccountService implements AccountServiceImp {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    AccountRepository accountRepository;

    @Autowired
    CloudinaryService cloudinaryService;

    @Override
    public AccountDTO createAccount(AccountCreateRequest request, MultipartFile avatar) {

        if (accountRepository.existsByPhone(request.getPhone()))
            throw new PhoneAlreadyExistsException("Phone number already exists");

        if(accountRepository.existsByUserName(request.getUserName()))
            throw new UserNameAlreadyExistsException("User name already exists");

        // length = 60
        String hashedPassword = passwordEncoder.encode(request.getPassword());

        Account account = Account.builder()
                .userName(request.getUserName())
                .email(request.getEmail())
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .phone(request.getPhone())
                .accountStatus(request.getStatus())
                .password(hashedPassword)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        if (avatar != null && !avatar.isEmpty()) {
            String avatarUploadedUrl = cloudinaryService.uploadFile(avatar, "real_estate_account");

            if (avatarUploadedUrl == null || avatarUploadedUrl.isEmpty())
                throw new CloudinaryUploadFailedException("Failed to upload images to Cloudinary\"");

            account.setAvatarUrl(avatarUploadedUrl);
        }

        Account savedAccount = accountRepository.save(account);

        return new AccountDTO(savedAccount.getAccountId(), savedAccount.getUserName(), savedAccount.getPhone(), savedAccount.getAccountStatus(),
                              savedAccount.getFirstName(), savedAccount.getLastName(), savedAccount.getEmail(), savedAccount.getAvatarUrl());
    }

    @Override
    public AccountDTO updateAccount(Integer id, AccountUpdateRequest request, MultipartFile avatarFile) {
        return accountRepository.findByAccountIdAndDeleted(id, false)
                .map(existingAccount -> {
                    updateAccountDetails(existingAccount, request);
                    updateAvatar(existingAccount, avatarFile);

                    return accountRepository.save(existingAccount);
                })
                .map(this::convertToAccountDTO)
                .orElseThrow(() -> new AccountNotFoundException("Cannot find account with id: " + id));
    }


    public List<AccountDTO> getAccountsByStatus(AccountStatusEnum status, Integer pageSize) {
        PageRequest pageRequest = PageRequest.of(0, pageSize, Sort.by("accountId"));
        Page<Account> accounts = accountRepository.findByAccountStatusAndDeleted(status, false, pageRequest);

        return accounts.stream()
                .map(account -> new AccountDTO(account.getAccountId(), account.getUserName(), account.getPhone(), account.getAccountStatus(),
                        account.getFirstName(), account.getLastName(), account.getEmail(), account.getAvatarUrl()))
                .collect(Collectors.toList());
    }


    private void updateAvatar(Account existingAccount, MultipartFile avatarFile) {
        if (avatarFile != null && !avatarFile.isEmpty()) {
            String uploadedAvatarUrl = cloudinaryService.uploadFile(avatarFile, "real_estate_account");
            if (uploadedAvatarUrl == null || uploadedAvatarUrl.isEmpty()) {
                throw new CloudinaryUploadFailedException("Failed to upload images to Cloudinary");
            }
            existingAccount.setAvatarUrl(uploadedAvatarUrl);
        }
    }

    private void updateAccountDetails(Account existingAccount, AccountUpdateRequest request) {
        if (request != null) {
            if (request.getPhone() != null && accountRepository.existsByPhone(request.getPhone()))
                throw new PhoneAlreadyExistsException("Phone number already exists");

            if (request.getUserName() != null && accountRepository.existsByUserName(request.getUserName()))
                throw new UserNameAlreadyExistsException("User name already exists");

            // Use Optional for Null check
            Optional.ofNullable(request.getUserName()).ifPresent(existingAccount::setUserName);
            Optional.ofNullable(request.getFirstName()).ifPresent(existingAccount::setFirstName);
            Optional.ofNullable(request.getLastName()).ifPresent(existingAccount::setLastName);
            Optional.ofNullable(request.getPhone()).ifPresent(existingAccount::setPhone);
            Optional.ofNullable(request.getEmail()).ifPresent(existingAccount::setEmail);
            Optional.ofNullable(request.getStatus()).ifPresent(existingAccount::setAccountStatus);
            Optional.ofNullable(request.getPassword())
                    .map(passwordEncoder::encode)
                    .ifPresent(existingAccount::setPassword);

            if (request.getAvatarUrlRemove()) {
                existingAccount.setAvatarUrl(null);
            }
        }
    }

    private AccountDTO convertToAccountDTO(Account savedAccount) {
        return AccountDTO.builder()
                .accountId(savedAccount.getAccountId())
                .userName(savedAccount.getUserName())
                .firstName(savedAccount.getFirstName())
                .lastName(savedAccount.getLastName())
                .email(savedAccount.getEmail())
                .phone(savedAccount.getPhone())
                .accountStatus(savedAccount.getAccountStatus())
                .avatarUrl(savedAccount.getAvatarUrl())
                .build();
    }

}
