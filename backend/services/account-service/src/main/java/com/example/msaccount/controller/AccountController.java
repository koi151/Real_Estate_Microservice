package com.example.msaccount.controller;

import com.example.msaccount.model.request.admin.AccountUpdateRequest;
import com.example.msaccount.model.response.ResponseData;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import com.example.msaccount.service.AccountService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("${api.acc-prefix}")
@RequiredArgsConstructor
@Slf4j
public class AccountController {

    private final AccountService accountService;

    @GetMapping("/current-account")
    @PreAuthorize("hasAuthority('SCOPE_accounts_view')")
    public ResponseEntity<ResponseData> getCurrentAccountInfo(@RequestHeader("Authorization") String authorizationHeader) {
        return ResponseEntity.status(HttpStatus.OK)
            .body(ResponseData.builder()
                .data(accountService.getCurrentAccountInfo(authorizationHeader))
                .desc("Get current user information successful")
                .build());
    }

    @PatchMapping("/")
    public ResponseEntity<ResponseData> updateCurrentAccount(
        @RequestHeader("Authorization") String authorizationHeader,
        @RequestPart(required = false) @Valid AccountUpdateRequest account,
        @RequestPart(required = false) MultipartFile avatar
    ){
        return ResponseEntity.ok(
            ResponseData.builder()
                .data(accountService.updateCurrentAccount(authorizationHeader, account, avatar))
                .desc("Account updated successfully")
                .build()
        );
    }
}
