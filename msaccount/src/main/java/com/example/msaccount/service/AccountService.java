package com.example.msaccount.service;

import com.example.msaccount.dto.AccountDTO;
import com.example.msaccount.dto.payload.request.AccountRequest;
import com.example.msaccount.entity.Account;
import com.example.msaccount.repository.AccountRepository;
import com.example.msaccount.service.imp.AccountServiceImp;
import customExceptions.CloudinaryUploadFailedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

@Service
public class AccountService implements AccountServiceImp {


    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    AccountRepository accountRepository;

    @Autowired
    CloudinaryService cloudinaryService;

    @Override
    public AccountDTO createAccount(AccountRequest request, MultipartFile avatar) {
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
                throw new CloudinaryUploadFailedException("Cloudinary failed to upload image");

            account.setAvatarUrl(avatarUploadedUrl);
        }

        Account savedAccount = accountRepository.save(account);

        return new AccountDTO(savedAccount.getAccountId(), savedAccount.getUserName(), savedAccount.getPhone(), savedAccount.getAccountStatus(),
                              savedAccount.getFirstName(), savedAccount.getLastName(), savedAccount.getEmail(), savedAccount.getAvatarUrl());
    }
}
