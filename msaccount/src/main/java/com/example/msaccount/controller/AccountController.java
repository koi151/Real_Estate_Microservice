package com.example.msaccount.controller;

import com.example.msaccount.dto.AccountDTO;
import com.example.msaccount.dto.payload.ResponseData;
import com.example.msaccount.dto.payload.request.AccountCreateRequest;
import com.example.msaccount.dto.payload.request.AccountUpdateRequest;
import com.example.msaccount.entity.AccountStatusEnum;
import com.example.msaccount.service.imp.AccountServiceImp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/v1/account")
public class AccountController {

    @Autowired
    AccountServiceImp accountServiceImp;

    @PostMapping("/")
    public ResponseEntity<ResponseData> createAccount(
            @RequestPart AccountCreateRequest account,
            @RequestPart(required = false) MultipartFile avatar
    ) {
        ResponseData responseData = new ResponseData();

        responseData.setData(accountServiceImp.createAccount(account, avatar));
        responseData.setDesc("Success");

        return ResponseEntity.ok(responseData);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<ResponseData> updateAccount(
            @PathVariable(name = "id") Integer id,
            @RequestPart(required = false) AccountUpdateRequest account,
            @RequestPart(required = false) MultipartFile avatar
    ){
        ResponseData responseData = new ResponseData();

        responseData.setData(accountServiceImp.updateAccount(id, account, avatar));
        responseData.setDesc("Success");

        return ResponseEntity.ok(responseData);
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<ResponseData> getAccountByStatus(
            @PathVariable(name = "status") String status,
            @RequestParam(defaultValue = "4") Integer pageSize
    ) {
        AccountStatusEnum se = AccountStatusEnum.valueOf(status.toUpperCase());
        List<AccountDTO> accountsData = accountServiceImp.getAccountsByStatus(se, pageSize);

        ResponseData responseData = new ResponseData();
        responseData.setData(accountsData);
        responseData.setDesc((!accountsData.isEmpty()) ? "Success" : "No account " + status + " found");

        return ResponseEntity.ok(responseData);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ResponseData> deleteAccount(@PathVariable(name = "id") Integer id) {
        accountServiceImp.deleteAccount(id);

        ResponseData responseData = new ResponseData();
        responseData.setDesc("Account deleted successful");

        return ResponseEntity.ok(responseData);
    }

}
