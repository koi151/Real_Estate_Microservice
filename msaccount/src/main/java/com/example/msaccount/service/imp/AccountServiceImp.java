package com.example.msaccount.service.imp;

import com.example.msaccount.dto.AccountDTO;
import com.example.msaccount.dto.payload.request.AccountCreateRequest;
import com.example.msaccount.dto.payload.request.AccountUpdateRequest;
import com.example.msaccount.entity.Account;
import com.example.msaccount.entity.AccountStatusEnum;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface AccountServiceImp {
    AccountDTO createAccount(AccountCreateRequest request, MultipartFile avatar);
    AccountDTO updateAccount(Integer id, AccountUpdateRequest request, MultipartFile avatar);
    List<AccountDTO> getAccountsByStatus(AccountStatusEnum status, Integer pageSize);
    void deleteAccount(Integer id);
}
