package com.example.msaccount.controller;

import com.example.msaccount.dto.payload.ResponseData;
import com.example.msaccount.dto.payload.request.AccountRequest;
import com.example.msaccount.service.imp.AccountServiceImp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/v1/account")
public class AccountController {

    @Autowired
    AccountServiceImp accountServiceImp;

    @PostMapping("/")
    public ResponseEntity<ResponseData> createAccount(
            @RequestPart AccountRequest account,
            @RequestPart(required = false) MultipartFile avatar
    ) {
        ResponseData responseData = new ResponseData();

        responseData.setData(accountServiceImp.createAccount(account, avatar));
        responseData.setDesc("Success");

        return ResponseEntity.ok(responseData);
    }
}
