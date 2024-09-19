package com.example.msaccount.validator;

import com.example.msaccount.customExceptions.PasswordMismatchException;
import com.example.msaccount.model.request.admin.AccountCreateRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AccountValidator {
//    private final AccountRepository accountRepository;

    public void validateAccountCreateRequest(AccountCreateRequest request)  {
        if (request == null)
            throw new IllegalArgumentException("Account creation request cannot be null");

        if (!request.passwordRetype().equals(request.password()))
            throw new PasswordMismatchException("Retype password does not match");
//        if (accountRepository.existsByPhone(request.getPhone()))
//            throw new PhoneAlreadyExistsException("Phone number already exists");
//        if (accountRepository.existsByAccountName(request.getAccountName()))
//            throw new AccountAlreadyExistsException("Account name already exists");
    }
}
