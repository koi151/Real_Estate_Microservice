package com.example.msaccount.service.admin.impl;

import com.example.msaccount.component.JwtTokenUtil;
import com.example.msaccount.customExceptions.*;
import com.example.msaccount.model.dto.AccountCreateDTO;
import com.example.msaccount.model.dto.AccountSearchDTO;
import com.example.msaccount.model.request.AccountCreateRequest;
import com.example.msaccount.model.request.AccountUpdateRequest;
import com.example.msaccount.entity.Account;
import com.example.msaccount.enums.AccountStatusEnum;
import com.example.msaccount.repository.AccountRepository;
import com.example.msaccount.service.admin.AccountService;
import com.example.msaccount.service.converter.AccountConverter;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class AccountServiceImpl implements AccountService {

    private final PasswordEncoder passwordEncoder;
    private final AccountRepository accountRepository;
    private final CloudinaryServiceImpl cloudinaryServiceImpl;
    private final AccountConverter accountConverter;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenUtil jwtTokenUtil;

    private void validateAccountCreateRequest(AccountCreateRequest request)  {
        if (accountRepository.existsByPhone(request.getPhone())) {
            throw new PhoneAlreadyExistsException("Phone number already exists");
        }
        if (accountRepository.existsByAccountName(request.getAccountName())) {
            throw new AccountAlreadyExistsException("Account name already exists");
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
    public String login(String accountName, String password) throws Exception {
        try {
            Account existingAccount = accountRepository.findByAccountNameAndDeleted(accountName, false)
                    .orElseThrow(() -> new AccountNotFoundException("Wrong account name or password"));

            if (existingAccount.getFacebookAccountId() == 0 && existingAccount.getGoogleAccountId() == 0) {
                if (!passwordEncoder.matches(password, existingAccount.getPassword()))
                    throw new BadCredentialsException("Wrong phone number or password");
            }

            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                    accountName, password, existingAccount.getAuthorities()
            );

            // authentication with Java Spring security
            authenticationManager.authenticate(authenticationToken);
            return jwtTokenUtil.generateToken(existingAccount);

        } catch (Exception ex) {
            System.out.println("Error occurred in service: " + ex.getMessage());
            return null;  // tempo
        }
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
                .map(accountEntity -> new AccountSearchDTO(accountEntity.getAccountId(), accountEntity.getAccountName(), accountEntity.getPhone(), accountEntity.getAccountStatus(),
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

            if (request.getAccountName() != null && accountRepository.existsByAccountName(request.getAccountName()))
                throw new AccountAlreadyExistsException("Account name already exists");

            // Use Optional for Null check
            Optional.ofNullable(request.getAccountName()).ifPresent(existingAccount::setAccountName);
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
                .accountName(savedAccount.getAccountName())
                .firstName(savedAccount.getFirstName())
                .lastName(savedAccount.getLastName())
                .email(savedAccount.getEmail())
                .phone(savedAccount.getPhone())
                .accountStatus(savedAccount.getAccountStatus())
                .avatarUrl(savedAccount.getAvatarUrl())
                .build();
    }

}
