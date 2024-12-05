package com.example.msaccount.service;

import com.example.msaccount.model.dto.LoginResponseDTO;
import com.example.msaccount.model.dto.TokenResponseDTO;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.util.MultiValueMap;

import java.util.Map;

public interface AuthService {
//    LoginResponse login(LoginRequest request);
    String redirectToLogin(String codeChallenge,String codeChallengeMethod,String state);
//    LoginResponseDTO handleOAuthCallback(String authCode, String state, HttpServletResponse response);

    TokenResponseDTO refreshToken(HttpServletRequest request);

    void exchangeToken(MultiValueMap<String, String> requestBody, HttpServletResponse response);
}
