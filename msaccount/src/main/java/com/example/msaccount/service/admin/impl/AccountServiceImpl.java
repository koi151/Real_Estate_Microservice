package com.example.msaccount.service.admin.impl;

import com.example.msaccount.customExceptions.CloudinaryUploadFailedException;
import com.example.msaccount.customExceptions.PhoneAlreadyExistsException;
import com.example.msaccount.customExceptions.UserNameAlreadyExistsException;
import com.example.msaccount.model.dto.AccountCreateDTO;
import com.example.msaccount.model.dto.AccountSearchDTO;
import com.example.msaccount.model.request.AccountCreateRequest;
import com.example.msaccount.model.request.AccountUpdateRequest;
import com.example.msaccount.entity.Account;
import com.example.msaccount.enums.AccountStatusEnum;
import com.example.msaccount.repository.AccountRepository;
import com.example.msaccount.service.admin.AccountService;
import com.example.msaccount.service.converter.AccountConverter;
import com.example.msaccount.customExceptions.AccountNotFoundException;
import jakarta.transaction.Transactional;
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
@Transactional
public class AccountServiceImpl implements AccountService {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private CloudinaryServiceImpl cloudinaryServiceImpl;

    @Autowired
    private AccountConverter accountConverter;

    private void validateAccountCreateRequest(AccountCreateRequest request)  {
        if (accountRepository.existsByPhone(request.getPhone())) {
            throw new PhoneAlreadyExistsException("Phone number already exists");
        }
        if (accountRepository.existsByUserName(request.getUserName())) {
            throw new UserNameAlreadyExistsException("User name already exists");
        }
    }

    @Override
    public AccountCreateDTO createAccount(AccountCreateRequest request, MultipartFile avatar)  {
        validateAccountCreateRequest(request);

        Account newAccount = accountConverter.toAccountEntity(request, avatar);
        accountRepository.save(newAccount);

        // develop: in case of current account do not have permission to create account

        return accountConverter.toAccountDTO(newAccount);
    }

    @Override
    public AccountCreateDTO updateAccount(Long id, AccountUpdateRequest request, MultipartFile avatarFile) {
        return accountRepository.findByAccountIdAndDeleted(id, false)
                .map(existingAccountEntity -> {
                    updateAccountDetails(existingAccountEntity, request);
                    updateAvatar(existingAccountEntity, avatarFile);

                    return accountRepository.save(existingAccountEntity);
                })
                .map(this::convertToAccountDTO)
                .orElseThrow(() -> new AccountNotFoundException("Cannot find account with id: " + id));
    }


    public List<AccountSearchDTO> getAccountsByStatus(AccountStatusEnum status, Integer pageSize) {
        PageRequest pageRequest = PageRequest.of(0, pageSize, Sort.by("accountId"));
        Page<Account> accounts = accountRepository.findByAccountStatusAndDeleted(status, false, pageRequest);

        return accounts.stream()
                .map(accountEntity -> new AccountSearchDTO(accountEntity.getAccountId(), accountEntity.getUserName(), accountEntity.getPhone(), accountEntity.getAccountStatus(),
                        accountEntity.getFirstName(), accountEntity.getLastName(), accountEntity.getEmail(), accountEntity.getAvatarUrl()))
                .collect(Collectors.toList());
    }

    @Override
    public void deleteAccount(Long id) {
        Account account = accountRepository.findById(id)
                .orElseThrow(() -> new AccountNotFoundException("Account with id " + id + " not found"));

        account.setDeleted(true);
        accountRepository.save(account);
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

    private AccountCreateDTO convertToAccountDTO(Account savedAccount) {
        return AccountCreateDTO.builder()
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
