package com.example.msaccount.controller;

import lombok.RequiredArgsConstructor;
import com.example.msaccount.service.admin.AccountService;
import org.springframework.web.bind.annotation.*;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("${api.acc-prefix}")
@RequiredArgsConstructor
@Slf4j
public class AccountController {

    private final AccountService accountService;



}
