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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/v1/admin/accounts")
public class AdminAccountController {

    @Autowired
    AccountService accountService;

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

    @PostMapping("/")
    public ResponseEntity<?> createAccount(
            @RequestPart @Valid AccountCreateRequest account,
            @RequestPart(required = false) MultipartFile avatar
//            BindingResult result
    ) {
            ResponseData responseData = new ResponseData();

//            if (result.hasErrors()) {
//                List<String> errorMessages = result.getFieldErrors()
//                        .stream()
//                        .map(FieldError::getDefaultMessage)
//                        .toList();
//                return ResponseEntity.badRequest().body(errorMessages);
//            }

            AccountDTO accountCreated = accountService.createAccount(account, avatar);

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
