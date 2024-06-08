package com.example.msaccount.controller;

import com.example.msaccount.dto.payload.ResponseData;
import com.example.msaccount.dto.payload.request.AccountCreateRequest;
import com.example.msaccount.dto.payload.request.AccountUpdateRequest;
import com.example.msaccount.service.imp.AccountServiceImp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

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

}
