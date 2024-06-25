package com.example.msaccount.service.admin;

import com.example.msaccount.model.dto.AccountCreateDTO;
import com.example.msaccount.model.dto.AccountSearchDTO;
import com.example.msaccount.model.request.AccountCreateRequest;
import com.example.msaccount.model.request.AccountUpdateRequest;
import com.example.msaccount.enums.AccountStatusEnum;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface AccountService {
    AccountCreateDTO createAccount(AccountCreateRequest request, MultipartFile avatar);
    AccountCreateDTO updateAccount(Long id, AccountUpdateRequest request, MultipartFile avatar);
    List<AccountSearchDTO> getAccountsByStatus(AccountStatusEnum status, Integer pageSize);
    void deleteAccount(Long id);
}
