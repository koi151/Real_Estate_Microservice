package com.example.msaccount.service.admin;

import com.example.msaccount.model.dto.AccountDTO;
import com.example.msaccount.model.dto.AccountWithNameAndRoleDTO;
import com.example.msaccount.model.request.admin.AccountCreateRequest;
import jakarta.validation.constraints.NotNull;
import jakarta.ws.rs.BadRequestException;
import org.keycloak.representations.AccessTokenResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.multipart.MultipartFile;

public interface AccountService {
//    List<AdminAccountDTO> findAllAdminAccounts();
//    AccountDTO findAccountDetails(Long accountId);
    AccountWithNameAndRoleDTO findAccountNameAndRoleById(String uuid);
//    Page<AccountWithPropertiesDTO> findAccountWithProperties(Long id, Pageable pageable);
    AccountDTO createAccount(AccountCreateRequest request, MultipartFile avatar);

    AccountDTO getCurrentAccountInfo(String authorizationHeader);
//    ===String login(String userName, String password) throws Exception;
//    AccountDTO updateAccount(AccountUpdateRequest request, MultipartFile avatar);
//    List<AccountSearchDTO> getAccountsByStatus(AccountStatusEnum status, Integer pageSize);
//    void deleteAdminAccount(Long id);
}