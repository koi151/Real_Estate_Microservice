package com.example.msaccount.service.imp;

import com.example.msaccount.dto.AccountDTO;
import com.example.msaccount.dto.payload.request.AccountCreateRequest;
import com.example.msaccount.dto.payload.request.AccountUpdateRequest;
import com.example.msaccount.entity.Account;
import org.springframework.web.multipart.MultipartFile;

public interface AccountServiceImp {
    AccountDTO createAccount(AccountCreateRequest request, MultipartFile avatar);
    AccountDTO updateAccount(Integer id, AccountUpdateRequest request, MultipartFile avatar);
}
