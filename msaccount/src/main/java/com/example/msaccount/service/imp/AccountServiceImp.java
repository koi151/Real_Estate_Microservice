package com.example.msaccount.service.imp;

import com.example.msaccount.dto.AccountDTO;
import com.example.msaccount.dto.payload.request.AccountRequest;
import org.springframework.web.multipart.MultipartFile;

public interface AccountServiceImp {

    AccountDTO createAccount(AccountRequest request, MultipartFile avatar);
}
