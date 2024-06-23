package com.example.msaccount.service.admin;

import com.example.msaccount.dto.AccountDTO;
import com.example.msaccount.dto.payload.request.AccountCreateRequest;
import com.example.msaccount.dto.payload.request.AccountUpdateRequest;
import com.example.msaccount.enums.AccountStatusEnum;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface AccountService {
    AccountDTO createAccount(AccountCreateRequest request, MultipartFile avatar) throws Exception;
    AccountDTO updateAccount(Integer id, AccountUpdateRequest request, MultipartFile avatar);
    List<AccountDTO> getAccountsByStatus(AccountStatusEnum status, Integer pageSize);
    void deleteAccount(Integer id);
}
