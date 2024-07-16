package com.example.msaccount.controller.admin;

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
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/v1/admin/accounts")
@RequiredArgsConstructor
public class AdminAccountController {

    private final AccountService accountService;

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody AccountLoginRequest request) {
        try {
            ResponseData responseData = new ResponseData();
            responseData.setData(accountService.login(request.getAccountName(), request.getPassword()));
            responseData.setDesc("Account login successful");

            return new ResponseEntity<>(responseData, HttpStatus.ACCEPTED);
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
        }
    }

    @GetMapping("/")
    public ResponseEntity<ResponseData> findAllAdminAccounts() {
        var accounts = accountService.findAllAdminAccounts();

        ResponseData responseData = new ResponseData();
        responseData.setData(accounts);
        responseData.setDesc(!accounts.isEmpty() ? "Success" : "No admin account found");

        return ResponseEntity.ok(responseData);
    }

    @GetMapping("/{id}/properties")
    public ResponseEntity<ResponseData> findAccountWithProperties(
            @PathVariable(name = "id") Long id,
            @RequestParam(name = "page", defaultValue = "1") Integer page,
            @RequestParam(name = "limit", defaultValue = "10") Integer limit)
    {
        Pageable pageable = PageRequest.of(page - 1, limit);
        var accountPage = accountService.findAccountWithProperties(id, pageable);
        var accountProperties = accountPage.getContent().get(0).getProperties();

        ResponseData responseData = new ResponseData();
        responseData.setData(accountPage.getContent());
        responseData.setTotalItems(accountPage.getTotalElements());

        responseData.setMaxPageItems(limit);
        responseData.setTotalPages(accountPage.getTotalPages());
        responseData.setCurrentPage(accountPage.getNumber() + 1);

        if (accountPage.hasContent() && !accountProperties.isEmpty())
            responseData.setDesc("Account with properties found.");
        else
            responseData.setDesc("Account has no property post.");

        return ResponseEntity.ok(responseData);
    }

    @GetMapping("/{account-id}/name-and-role")
    public ResponseEntity<ResponseData> getAccountNameAndRole(@PathVariable(name = "account-id") Long accountId) {
        var account = accountService.getAccountNameAndRole(accountId);

        ResponseData responseData = new ResponseData();
        responseData.setData(account);
        responseData.setDesc("Get account with name and role succeed");

        return ResponseEntity.ok(responseData);
    }

    @PostMapping("/")
    public ResponseEntity<ResponseData> createAccount(
            @RequestPart @Valid AccountCreateRequest account,
            @RequestPart(required = false) MultipartFile avatar
    ) {
            var accountCreated = accountService.createAccount(account, avatar);

            ResponseData responseData = new ResponseData();
            responseData.setData(accountCreated);
            responseData.setDesc("Success");

            return ResponseEntity.status(HttpStatus.CREATED).body(responseData);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<ResponseData> updateAccount(
            @PathVariable(name = "id") Long id,
            @RequestPart(required = false) AccountUpdateRequest account,
            @RequestPart(required = false) MultipartFile avatar
    ){
        ResponseData responseData = new ResponseData();

        responseData.setData(accountService.updateAccount(id, account, avatar));
        responseData.setDesc("Success");

        return ResponseEntity.ok(responseData);
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<ResponseData> getAccountByStatus(
            @PathVariable(name = "status") String status,
            @RequestParam(defaultValue = "4") Integer pageSize
    ) {
        AccountStatusEnum se = AccountStatusEnum.valueOf(status.toUpperCase());
        List<AccountSearchDTO> accountsData = accountService.getAccountsByStatus(se, pageSize);

        ResponseData responseData = new ResponseData();
        responseData.setData(accountsData);
        responseData.setDesc((!accountsData.isEmpty()) ? "Success" : "No account " + status + " found");

        return ResponseEntity.ok(responseData);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ResponseData> deleteAdminAccount(@PathVariable(name = "id") Long id) {
        accountService.deleteAdminAccount(id);

        ResponseData responseData = new ResponseData();
        responseData.setDesc("Account deleted successful");

        return ResponseEntity.ok(responseData);
    }

}
