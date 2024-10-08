package com.example.msaccount.validator;

import com.example.msaccount.customExceptions.InvalidRequestException;
import com.example.msaccount.customExceptions.PasswordMismatchException;
import com.example.msaccount.model.request.admin.AccountCreateRequest;
import com.example.msaccount.model.request.admin.AccountUpdateRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

@Component
@RequiredArgsConstructor
public class AccountValidator {
//    private final AccountRepository accountRepository;

    public void validateAccountCreateRequest(AccountCreateRequest request)  {
        if (request == null)
            throw new IllegalArgumentException("Account creation request cannot be null");
        if (!request.passwordRetype().equals(request.password()))
            throw new PasswordMismatchException("Retype password does not match");
    }

    public void validateAccountUpdateRequest(AccountUpdateRequest request, MultipartFile avatar)  {
        if (request == null && avatar == null)
            throw new InvalidRequestException("Either new updated field or avatar must be included in the request");
    }
}
