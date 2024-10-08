package com.example.msaccount.service.admin;

import com.example.msaccount.model.dto.AccountDetailDTO;
import com.example.msaccount.model.dto.AccountWithNameAndRoleDTO;
import com.example.msaccount.model.request.admin.AccountCreateRequest;
import com.example.msaccount.model.request.admin.AccountUpdateRequest;
import org.springframework.web.multipart.MultipartFile;

public interface AccountAdminService {
//    List<AdminAccountDTO> findAllAdminAccounts();
//    AccountDTO findAccountDetails(Long accountId);
    AccountDetailDTO createAccountAdmin(AccountCreateRequest request, MultipartFile avatar);

    AccountWithNameAndRoleDTO findAccountNameAndRoleById(String uuid);
//    Page<AccountWithPropertiesDTO> findAccountWithProperties(Long id, Pageable pageable);
    AccountDetailDTO getCurrentAccountInfo(String authorizationHeader);
//    ===String login(String userName, String password) throws Exception;
    AccountDetailDTO updateAccount(String accountId, AccountUpdateRequest request, MultipartFile avatar);
//    List<AccountSearchDTO> getAccountsByStatus(AccountStatusEnum status, Integer pageSize);
//    void deleteAdminAccount(Long id);
}