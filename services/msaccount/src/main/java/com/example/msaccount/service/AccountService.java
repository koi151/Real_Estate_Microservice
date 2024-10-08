package com.example.msaccount.service;

import com.example.msaccount.model.dto.AccountDetailDTO;
import com.example.msaccount.model.dto.AccountWithNameAndRoleDTO;
import com.example.msaccount.model.request.admin.AccountCreateRequest;
import com.example.msaccount.model.request.admin.AccountUpdateRequest;
import org.springframework.web.multipart.MultipartFile;

public interface AccountService {
//    List<AdminAccountDTO> findAllAdminAccounts();
//    AccountDTO findAccountDetails(Long accountId);
    AccountWithNameAndRoleDTO findAccountNameAndRoleById(String uuid);
//    Page<AccountWithPropertiesDTO> findAccountWithProperties(Long id, Pageable pageable);
    AccountDetailDTO createAccount(AccountCreateRequest request, MultipartFile avatar);

    AccountDetailDTO getCurrentAccountInfo(String authorizationHeader);
//    ===String login(String userName, String password) throws Exception;
    AccountDetailDTO updateCurrentAccount(String authorizationHeader, AccountUpdateRequest request, MultipartFile avatar);
//    List<AccountSearchDTO> getAccountsByStatus(AccountStatusEnum status, Integer pageSize);
//    void deleteAdminAccount(Long id);
}