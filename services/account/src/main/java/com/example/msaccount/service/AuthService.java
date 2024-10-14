package com.example.msaccount.service;

import com.example.msaccount.model.request.LoginRequest;
import org.keycloak.representations.AccessTokenResponse;

public interface AuthService {
    AccessTokenResponse login(LoginRequest request);
}
