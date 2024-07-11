package com.example.msaccount.service.admin.impl;

import com.example.msaccount.client.PropertiesClient;
import com.example.msaccount.component.JwtTokenUtil;
import com.example.msaccount.customExceptions.*;
import com.example.msaccount.entity.admin.AdminAccount;
import com.example.msaccount.mapper.AccountMapper;
import com.example.msaccount.model.dto.AccountDTO;
import com.example.msaccount.model.dto.AccountSearchDTO;
import com.example.msaccount.model.dto.AccountWithPropertiesDTO;
import com.example.msaccount.model.dto.PropertyDTO;
import com.example.msaccount.model.dto.admin.AdminAccountDTO;
import com.example.msaccount.model.request.AccountCreateRequest;
import com.example.msaccount.model.request.AccountUpdateRequest;
import com.example.msaccount.entity.Account;
import com.example.msaccount.enums.AccountStatusEnum;
import com.example.msaccount.model.response.ResponseData;
import com.example.msaccount.repository.AccountRepository;
import com.example.msaccount.repository.admin.AdminAccountRepository;
import com.example.msaccount.service.admin.AccountService;
import com.example.msaccount.service.converter.AccountConverter;
import com.example.msaccount.service.converter.admin.AdminAccountConverter;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.http.ResponseEntity;
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
    private final AdminAccountRepository adminAccountRepository;
    private final CloudinaryServiceImpl cloudinaryServiceImpl;
    private final AdminAccountConverter adminAccountConverter;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenUtil jwtTokenUtil;
    private final PropertiesClient propertiesClient;

    private final AccountConverter accountConverter;
    private final AccountMapper accountMapper;
    private final ObjectMapper objectMapper;


    private void validateAccountCreateRequest(AccountCreateRequest request)  {
        if (!request.getRetypePassword().equals(request.getPassword()))
            throw new PasswordMismatchException("Retype password does not match");
        if (accountRepository.existsByPhone(request.getPhone()))
            throw new PhoneAlreadyExistsException("Phone number already exists");
        if (accountRepository.existsByAccountName(request.getAccountName()))
            throw new AccountAlreadyExistsException("Account name already exists");
    }

    @Override
    public List<AdminAccountDTO> findAllAdminAccounts() {
        return adminAccountRepository.findAllByAccountDeleted(false, Sort.by("accountId")).stream()
                .map(adminAccountConverter::toAdminAccountDTO)
                .collect(Collectors.toList());
    }

//    @Override
//    public Page<AccountWithPropertiesDTO> findAccountWithProperties(Long accountId, Pageable pageable) {
//
//        Account account = accountRepository.findByAccountId(accountId)
//                .orElseThrow(() -> new AccountNotFoundException("Account not found with id: " + accountId));
//
//        // Fetch Properties using Feign Client
//        ResponseEntity<ResponseData> response = propertiesClient.findAllPropertiesByAccount(accountId);
//
//        if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
//
//            ResponseEntity<ResponseData> responseEntity = propertiesClient.findAllPropertiesByAccount(accountId);
//            ResponseData responseData = Objects.requireNonNull(responseEntity.getBody());
//
//            List<PropertyDTO> properties;
//            try { // convert object to List<Properties>
////                properties = objectMapper.convertValue(responseData.getData(), new TypeReference<>() {});
//                Object dataObject = responseData.getData();
//
//
//
//            } catch (Exception e) {
//                throw new RuntimeException("Failed to deserialize properties data", e);
//            }
//
//            return null;
//        } else {
//            throw new RuntimeException("Failed to fetch properties from properties service");
//        }
//    }

    @Override
    public Page<AccountWithPropertiesDTO> findAccountWithProperties(Long accountId, Pageable pageable) {
        // Fetch Account (only if you need account details)
        Account account = accountRepository.findByAccountId(accountId)
                .orElseThrow(() -> new AccountNotFoundException("Account not found with id: " + accountId));

        // Fetch Properties using Feign Client
        ResponseEntity<ResponseData> response = propertiesClient.findAllPropertiesByAccount(accountId);

        if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
            try {
                ResponseData responseData = response.getBody();
                Object dataObject = responseData.getData();

                if (dataObject instanceof List<?> dataList) {
                    // Convert to List<PropertyDTO> using ObjectMapper
                    List<PropertyDTO> properties = dataList.stream()
                            .map(item -> objectMapper.convertValue(item, PropertyDTO.class))
                            .toList();

                    // Construct AccountWithPropertiesDTO using MapStruct
                    AccountWithPropertiesDTO accountWithPropertiesDTO = accountMapper.toAccountWithPropertiesDTO(account, properties);

                    // Wrap the result in a Page object
                    return new PageImpl<>(List.of(accountWithPropertiesDTO), pageable, 1);
                } else {
                    throw new RuntimeException("Unexpected data format (not a list) received from properties service");
                }
            } catch (Exception ex) {
                throw new RuntimeException("Error parsing property data: " + ex.getMessage(), ex);
            }
        } else {
            throw new RuntimeException("Failed to fetch properties from properties service");
        }
    }


    @Override
    public AccountDTO createAccount(AccountCreateRequest request, MultipartFile avatar)  {
        validateAccountCreateRequest(request);

        Account newAccount = accountConverter.toAccountEntity(request, avatar);
        accountRepository.save(newAccount);

        return accountConverter.toAccountDTO(newAccount);
    }

    @Override
    public String login(String accountName, String password) {
        Account existingAccount = accountRepository.findByAccountName(accountName) // del
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
    }

    @Override
    public AccountDTO updateAccount(Long id, AccountUpdateRequest request, MultipartFile avatarFile) {
        return accountRepository.findByAccountId(id) // del
                .map(existingAccountEntity -> {
                    updateAccountDetails(existingAccountEntity, request);
                    updateAvatar(existingAccountEntity, avatarFile);

                    return accountRepository.save(existingAccountEntity);
                })
                .map(this::convertToAccountDTO)
                .orElseThrow(() -> new AccountNotFoundException("Cannot find account with id: " + id));
    }


    public List<AccountSearchDTO> getAccountsByStatus(AccountStatusEnum status, Integer pageSize) {
//        PageRequest pageRequest = PageRequest.of(0, pageSize, Sort.by("accountId")); tempo cmt
        List<AdminAccount> accounts = adminAccountRepository.findByAccountStatusDeleted(status, false, Sort.by("accountId"));

        return accounts.stream()
                .map(adminAccountConverter::toAccountSearchDTO)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteAdminAccount(Long id) {
        Account account = accountRepository.findById(id)
                .orElseThrow(() -> new AccountNotFoundException("Admin account with id " + id + " not found"));

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

    private AccountDTO convertToAccountDTO(Account savedAccount) {
        return AccountDTO.builder()
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
