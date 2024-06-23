package com.example.msaccount.controller.admin;

import com.example.msaccount.dto.AccountDTO;
import com.example.msaccount.dto.payload.ResponseData;
import com.example.msaccount.dto.payload.request.AccountCreateRequest;
import com.example.msaccount.dto.payload.request.AccountUpdateRequest;
import com.example.msaccount.enums.AccountStatusEnum;
import com.example.msaccount.service.admin.AccountService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping(path = "api/v1/admin/account")
public class AdminAccountController {

    @Autowired
    AccountService accountService;

    @PostMapping("/")
    public ResponseEntity<?> createAccount(
            @RequestPart @Valid AccountCreateRequest account, // recheck
            @RequestPart(required = false) MultipartFile avatar,
            BindingResult result
    ) {
        try {
            if (result.hasErrors()) {
                List<String> errorMessages = result.getFieldErrors()
                        .stream()
                        .map(FieldError::getDefaultMessage)
                        .toList();
                return ResponseEntity.badRequest().body(errorMessages);
            }

            AccountDTO accountCreated = accountService.createAccount(account, avatar);

            ResponseData responseData = new ResponseData();
            responseData.setData(accountCreated);
            responseData.setDesc("Success");

            return ResponseEntity.ok(responseData);

        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
        }
    }

    @PatchMapping("/{id}")
    public ResponseEntity<ResponseData> updateAccount(
            @PathVariable(name = "id") Integer id,
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
        List<AccountDTO> accountsData = accountService.getAccountsByStatus(se, pageSize);

        ResponseData responseData = new ResponseData();
        responseData.setData(accountsData);
        responseData.setDesc((!accountsData.isEmpty()) ? "Success" : "No account " + status + " found");

        return ResponseEntity.ok(responseData);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ResponseData> deleteAccount(@PathVariable(name = "id") Integer id) {
        accountService.deleteAccount(id);

        ResponseData responseData = new ResponseData();
        responseData.setDesc("Account deleted successful");

        return ResponseEntity.ok(responseData);
    }

}
