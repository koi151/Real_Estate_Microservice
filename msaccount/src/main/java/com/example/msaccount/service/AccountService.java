package com.example.msaccount.service;

import com.example.msaccount.dto.AccountDTO;
import com.example.msaccount.dto.payload.request.AccountCreateRequest;
import com.example.msaccount.dto.payload.request.AccountUpdateRequest;
import com.example.msaccount.entity.Account;
import com.example.msaccount.repository.AccountRepository;
import com.example.msaccount.service.imp.AccountServiceImp;
import customExceptions.AccountNotFoundException;
import customExceptions.CloudinaryUploadFailedException;
import customExceptions.PhoneAlreadyExistsException;
import customExceptions.UserNameAlreadyExistsException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;

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
                    if (request != null) {
                        if (accountRepository.existsByPhone(request.getPhone()))
                            throw new PhoneAlreadyExistsException("Phone number already exists");

                        if(accountRepository.existsByUserName(request.getUserName()))
                            throw new UserNameAlreadyExistsException("User name already exists");

                        if (request.getUserName() != null)
                            existingAccount.setUserName(request.getUserName());
                        if (request.getFirstName() != null)
                            existingAccount.setFirstName(request.getFirstName());
                        if (request.getLastName() != null)
                            existingAccount.setLastName(request.getLastName());
                        if (request.getPhone() != null)
                            existingAccount.setPhone(request.getPhone());
                        if (request.getEmail() != null)
                            existingAccount.setEmail(request.getEmail());
                        if (request.getStatus() != null)
                            existingAccount.setAccountStatus(request.getStatus());

                        if (request.getPassword() != null) {
                            String hashedPassword = passwordEncoder.encode(request.getPassword());
                            existingAccount.setPassword(hashedPassword);
                        }
                    }

                    if (avatarFile != null && !avatarFile.isEmpty()) {
                        String uploadedAvatarUrl = cloudinaryService.uploadFile(avatarFile, "real_estate_account");
                        if (uploadedAvatarUrl == null || uploadedAvatarUrl.isEmpty())
                            throw new CloudinaryUploadFailedException("Failed to upload images to Cloudinary");

                        existingAccount.setAvatarUrl(uploadedAvatarUrl);
                    }

                    return accountRepository.save(existingAccount);
                })
                .map(savedAccount -> AccountDTO.builder()
                        .accountId(savedAccount.getAccountId())
                        .userName(savedAccount.getUserName())
                        .firstName(savedAccount.getFirstName())
                        .lastName(savedAccount.getLastName())
                        .email(savedAccount.getEmail())
                        .phone(savedAccount.getPhone())
                        .accountStatus(savedAccount.getAccountStatus())
                        .avatarUrl(savedAccount.getAvatarUrl())
                        .build())
                .orElseThrow(() -> new AccountNotFoundException("Cannot found account with id: " + id));
    }
}
