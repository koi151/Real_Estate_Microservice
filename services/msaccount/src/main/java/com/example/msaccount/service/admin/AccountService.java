package com.example.msaccount.service.admin;

import com.example.msaccount.model.dto.AccountDTO;
import com.example.msaccount.model.request.admin.AccountUpdateRequest;
import com.example.msaccount.model.request.admin.AccountCreateRequest;
import org.springframework.web.multipart.MultipartFile;

public interface AccountService {
//    List<AdminAccountDTO> findAllAdminAccounts();
//    AccountDTO findAccountDetails(Long accountId);
//    AccountWithNameAndRoleDTO findAccountNameAndRoleById(Long id);
//    Page<AccountWithPropertiesDTO> findAccountWithProperties(Long id, Pageable pageable);
    AccountDTO createAccount(AccountCreateRequest request, MultipartFile avatar);
//    ===String login(String userName, String password) throws Exception;
    AccountDTO updateAccount(AccountUpdateRequest request, MultipartFile avatar);
//    List<AccountSearchDTO> getAccountsByStatus(AccountStatusEnum status, Integer pageSize);
//    void deleteAdminAccount(Long id);
}
