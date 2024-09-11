package com.example.msaccount.controller.admin;

import com.example.msaccount.mapper.PaginationContext;
import com.example.msaccount.mapper.ResponseDataMapper;
import com.example.msaccount.model.dto.AccountDTO;
import com.example.msaccount.model.dto.AccountSearchDTO;
import com.example.msaccount.model.request.AccountLoginRequest;
import com.example.msaccount.model.response.ResponseData;
import com.example.msaccount.model.request.AccountCreateRequest;
import com.example.msaccount.model.request.AccountUpdateRequest;
import com.example.msaccount.enums.AccountStatusEnum;
import com.example.msaccount.service.admin.AccountService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/v1/admin/accounts")
@RequiredArgsConstructor
public class AdminAccountController {

    private final AccountService accountService;
    private final ResponseDataMapper responseDataMapper;

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody AccountLoginRequest request) {
        try {
            ResponseData responseData = ResponseData.builder()
                .data(accountService.login(request.getAccountName(), request.getPassword()))
                .desc("Account login successful")
                .build();
            return new ResponseEntity<>(responseData, HttpStatus.ACCEPTED);
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
        }
    }

    @GetMapping("/")
    @PreAuthorize("hasAuthority('SCOPE_accounts_view')")
    public ResponseEntity<ResponseData> findAllAdminAccounts() {
        var accounts = accountService.findAllAdminAccounts();
        return ResponseEntity.ok(
            ResponseData.builder()
                .data(accounts)
                .desc(!accounts.isEmpty() ? "Success" : "No admin account found")
                .build()
        );
    }

    @GetMapping("/{id}/properties")
    @PreAuthorize("hasAuthority('SCOPE_accounts_view')")
    public ResponseEntity<ResponseData> findAccountWithProperties (
        @PathVariable(name = "id") Long id,
        @RequestParam(name = "page", defaultValue = "1") Integer page,
        @RequestParam(name = "limit", defaultValue = "10") Integer limit)
    {
        Pageable pageable = PageRequest.of(page - 1, limit);
        var accountPage = accountService.findAccountWithProperties(id, pageable);
        var accountProperties = accountPage.getContent().get(0).getProperties();

        PaginationContext paginationContext = new PaginationContext(page, limit);

        return ResponseEntity.ok(ResponseData.builder()
            .data(responseDataMapper.toResponseData(accountPage, paginationContext))
            .desc(accountPage.hasContent() && !accountProperties.isEmpty()
                ? "Account with properties found."
                : "Account has no property post.")
            .build());
    }

    @GetMapping("/{account-id}")
    @PreAuthorize("hasAuthority('SCOPE_accounts_view')")
    public ResponseEntity<ResponseData> findAccountDetails(@PathVariable(name = "account-id") Long accountId) {
        var account = accountService.findAccountDetails(accountId);
        return ResponseEntity.ok(
            ResponseData.builder()
                .data(account)
                .desc("Get account details succeed")
                .build()
        );
    }

    @GetMapping("/{account-id}/name-and-role")
    @PreAuthorize("hasAuthority('SCOPE_accounts_view')")
    public ResponseEntity<ResponseData> getAccountNameAndRole(@PathVariable(name = "account-id") Long accountId) {
        var account = accountService.findAccountNameAndRoleById(accountId);
        return ResponseEntity.ok(
                ResponseData.builder()
                    .data(account)
                    .desc("Get account with name and role succeed")
                    .build()
        );
    }

    @PostMapping("/")
    @PreAuthorize("hasAuthority('SCOPE_accounts_create')")
    public ResponseEntity<ResponseData> createAccount(
            @RequestPart @Valid AccountCreateRequest account,
            @RequestPart(required = false) MultipartFile avatar
    ) {
        var accountCreated = accountService.createAccount(account, avatar);
        return new ResponseEntity<>(
            ResponseData.builder()
                    .data(accountCreated)
                    .desc("Account created successfully")
                    .build()
            , HttpStatus.CREATED);
    }

    @PatchMapping("/{id}")
    @PreAuthorize("hasAuthority('SCOPE_accounts_update')")
    public ResponseEntity<ResponseData> updateAccount(
            @PathVariable(name = "id") Long id,
            @RequestPart(required = false) AccountUpdateRequest account,
            @RequestPart(required = false) MultipartFile avatar
    ){
        return ResponseEntity.ok(
            ResponseData.builder()
                .data(accountService.updateAccount(id, account, avatar))
                .desc("Account updated successfully")
                .build()
        );
    }

    @GetMapping("/status/{status}")
    @PreAuthorize("hasAuthority('SCOPE_accounts_view')")
    public ResponseEntity<ResponseData> getAccountByStatus(
            @PathVariable(name = "status") String status,
            @RequestParam(defaultValue = "4") Integer pageSize
    ) {
        AccountStatusEnum se = AccountStatusEnum.valueOf(status.toUpperCase());
        var accounts = accountService.getAccountsByStatus(se, pageSize);

        return ResponseEntity.ok(
            ResponseData.builder()
                .data(accounts)
                .desc((!accounts.isEmpty()) ? "Success" : "No account with status " + status + " found")
                .build()
        );
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('SCOPE_accounts_delete')")
    public ResponseEntity<ResponseData> deleteAdminAccount(@PathVariable(name = "id") Long id) {
        accountService.deleteAdminAccount(id);
        return ResponseEntity.ok(
            ResponseData.builder()
                .desc("Account deleted successful")
                .build()
        );
    }
}
