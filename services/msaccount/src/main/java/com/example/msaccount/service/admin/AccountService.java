package com.example.msaccount.service.admin;

import com.example.msaccount.model.dto.AccountDTO;
import com.example.msaccount.model.dto.AccountSearchDTO;
import com.example.msaccount.model.dto.AccountWithNameAndRoleDTO;
import com.example.msaccount.model.dto.AccountWithPropertiesDTO;
import com.example.msaccount.model.dto.admin.AdminAccountDTO;
import com.example.msaccount.model.request.AccountCreateRequest;
import com.example.msaccount.model.request.AccountUpdateRequest;
import com.example.msaccount.enums.AccountStatusEnum;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface AccountService {
    List<AdminAccountDTO> findAllAdminAccounts();
    AccountDTO findAccountDetails(Long accountId);
    AccountWithNameAndRoleDTO findAccountNameAndRoleById(Long id);
    Page<AccountWithPropertiesDTO> findAccountWithProperties(Long id, Pageable pageable);
    AccountDTO createAccount(AccountCreateRequest request, MultipartFile avatar);
    String login(String userName, String password) throws Exception;
    AccountDTO updateAccount(Long id, AccountUpdateRequest request, MultipartFile avatar);
    List<AccountSearchDTO> getAccountsByStatus(AccountStatusEnum status, Integer pageSize);
    void deleteAdminAccount(Long id);
}
