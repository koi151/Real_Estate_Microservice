package com.example.msaccount.service.impl.admin;

import com.example.msaccount.client.PropertiesClient;
import com.example.msaccount.customExceptions.*;
import com.example.msaccount.entity.Account;
import com.example.msaccount.mapper.AccountMapper;
import com.example.msaccount.model.dto.*;
import com.example.msaccount.model.request.admin.AccountCreateRequest;
import com.example.msaccount.model.request.admin.AccountUpdateRequest;
import com.example.msaccount.model.response.ResponseData;
import com.example.msaccount.repository.AccountRepository;

import com.example.msaccount.repository.admin.AdminAccountRepository;
import com.example.msaccount.repository.client.ClientAccountRepository;
import com.example.msaccount.service.admin.AccountAdminService;
import com.example.msaccount.service.KeycloakUserService;
import com.example.msaccount.service.converter.AccountConverter;
import com.example.msaccount.service.impl.AuthServiceImpl;
import com.example.msaccount.service.impl.CloudinaryServiceImpl;
import com.example.msaccount.validator.AccountValidator;
import com.fasterxml.jackson.databind.ObjectMapper;
import feign.FeignException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class AccountAdminServiceImpl implements AccountAdminService {

    private final AccountRepository accountRepository;
    private final AdminAccountRepository adminAccountRepository;
    private final ClientAccountRepository clientAccountRepository;

    private final CloudinaryServiceImpl cloudinaryServiceImpl;
    private final KeycloakUserService keycloakUserService;

    private final AccountMapper accountMapper;
    private final ObjectMapper objectMapper;

    private final AccountConverter accountConverter;
    private final AccountValidator accountValidator;
    private final RedisTemplate<String, Object> redisTemplate;
    private final PropertiesClient propertiesClient;


//    @Override
//    public List<AdminAccountDTO> findAllAdminAccounts() {
//        return adminAccountRepository.findAll(false, Sort.by("accountId")).stream()
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


    @Override
    @Transactional
    public AccountDetailDTO createAccountAdmin(AccountCreateRequest request, MultipartFile avatar)  {
        accountValidator.validateAccountCreateRequest(request);

        KeycloakUserDTO kcUserDTO = keycloakUserService.createUser(request);

        Account account = accountConverter.toEntity(request, kcUserDTO.id(), avatar);
        accountRepository.save(account);

        return accountMapper.requestToAccountDTO(account, kcUserDTO);
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
        AccountDetailDTO accountDetailDTOFromRedis = getAccountDataFromHeader(authorizationHeader);
        String accountId = accountDetailDTOFromRedis.getAccountId();

        return accountDetailDTOFromRedis.getIsAdmin()
            ? accountRepository.findAdminAccountInfoFromDB(accountId)
                .map(dbDto -> accountMapper.mapAdminDatabaseAccountDTO(dbDto, accountDetailDTOFromRedis))
                .orElseThrow(() -> new EntityNotFoundException("Cannot found admin account information in database with id: " + accountId))

            : accountRepository.findClientAccountInfoFromDB(accountId)
                .map(dbDto -> accountMapper.mapClientDatabaseAccountDTO(dbDto, accountDetailDTOFromRedis))
                .orElseThrow(() -> new EntityNotFoundException("Cannot found client account information in database with id: " + accountId));
    }


    @Override
    @Transactional
    public AccountDetailDTO updateAccount(String accountId, AccountUpdateRequest request, MultipartFile avatarFile) {
        KeycloakUserDTO kcDTO = keycloakUserService.updateAccount(request, accountId);

        return accountRepository.findByAccountIdAndAccountEnableAndDeleted(accountId, true, false)
            .map(existingEntity -> {
                accountMapper.updateAccountFromRequest(request, existingEntity);
                return accountRepository.save(existingEntity);
            })
            .map(entity -> accountMapper.entityToAccountDTO(entity, kcDTO))
            .orElseThrow(() -> new EntityNotFoundException("Account not found with id: " + accountId));
    }

    @Override
    public Page<AccountWithPropertiesDTO> findAccountWithProperties(String accountId, Pageable pageable) {

        Account account = accountRepository.findByAccountIdAndAccountEnableAndDeleted(accountId, true , false)
                .orElseThrow(() -> new EntityNotFoundException("Account not found with id: " + accountId));

        // Fetch Properties using Feign Client
        try {
            ResponseEntity<ResponseData> responseData = propertiesClient.findAllPropertiesByAccount(accountId, pageable.getPageNumber(), pageable.getPageSize());

            if (responseData.getStatusCode().is2xxSuccessful() && responseData.getBody() != null) {
                ResponseData responseBody = responseData.getBody();
                Object dataObject = responseBody.getData();

                if (dataObject instanceof List<?> dataList) {
                    List<PropertyDTO> properties = dataList.stream()
                        .map(item -> objectMapper.convertValue(item, PropertyDTO.class))
                        .toList();

                    AccountWithPropertiesDTO accountWithPropertiesDTO = accountMapper.toAccountWithPropertiesDTO(account, properties);
                    String username = keycloakUserService.retrieveUsernameByID(accountId);
                    accountWithPropertiesDTO.setAccountName(username);

                    return new PageImpl<>(List.of(accountWithPropertiesDTO), pageable, responseBody.getTotalItems()); // total properties count
                } else {
                    throw new RuntimeException("Unexpected data format (not a list) received from properties service");
                }
            } else {
                throw new RuntimeException("Failed to fetch properties from properties service");
            }
        } catch (FeignException ex) {
            log.error("Error occurred while fetching data from Property Service");
            throw new RemoteServiceException("Error occurred while fetching data from Property Service", ex);
        }
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

    @Override
    @Transactional
    public void deleteAccount(String id) {
        int updatedRows = accountRepository.softDeleteAccountById(id);
        if (updatedRows == 0) {
            throw new EntityNotFoundException("Cannot find account with id " + id);
        }

        int updatedAdminRows = adminAccountRepository.softDeleteAdminAccountById(id);
        int updatedClientRows = clientAccountRepository.softDeleteClientAccountByAccountId(id);

        if (updatedAdminRows == 0 && updatedClientRows == 0) {
            throw new EntityNotFoundException("No linked AdminAccount or ClientAccount found for account id " + id);
        }

        keycloakUserService.disableUserWithId(id);
    }

//    public List<AccountSearchDTO> getAccountsByStatus(AccountStatusEnum status, Integer pageSize) {
////        PageRequest pageRequest = PageRequest.of(0, pageSize, Sort.by("accountId")); tempo cmt
//        List<AdminAccount> accounts = adminAccountRepository.findByAccountStatusDeleted(status, false, Sort.by("accountId"));
//
//        return accounts.stream()
//                .map(adminAccountConverter::toAccountSearchDTO)
//                .collect(Collectors.toList());
//    }

}
