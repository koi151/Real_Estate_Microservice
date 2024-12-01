package com.example.msaccount.service;

import com.example.msaccount.model.dto.AccountDetailDTO;
import com.example.msaccount.model.request.LoginRequest;
import com.example.msaccount.model.response.LoginResponse;
import com.example.msaccount.model.response.UserInfoResponse;
import jakarta.servlet.http.HttpServletResponse;
import org.keycloak.representations.AccessTokenResponse;

public interface AuthService {
//    LoginResponse login(LoginRequest request);
    String redirectToLogin();
    AccountDetailDTO handleOAuthCallback(String authCode, String state, HttpServletResponse response);
}
