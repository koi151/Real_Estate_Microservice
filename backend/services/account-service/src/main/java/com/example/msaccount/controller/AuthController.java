package com.example.msaccount.controller;

import com.example.msaccount.model.response.ResponseData;
import com.example.msaccount.service.AuthService;
import com.example.msaccount.utils.PKCEUtil;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import jakarta.ws.rs.BadRequestException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.Charsets;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.*;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("${api.acc-prefix}/auth")
@RequiredArgsConstructor
@Slf4j
public class AuthController {

    private final AuthService authService;

//    @PostMapping("/login")
//    public ResponseEntity<ResponseData> login(@Valid @RequestBody LoginRequest loginRequest, HttpServletResponse response) {
//        try {
//            LoginResponse loginResponse = authService.login(loginRequest);
//            String accessToken = loginResponse.accessToken().getToken();
//
//            // Create HttpOnly Secure Cookie for accessToken
//            ResponseCookie cookie = ResponseCookie.from("auth_token", accessToken)
//                .httpOnly(true)
//                .secure(true) // only send through HTTPS in production env
//                .path("/")
//                .sameSite("Lax")
//                .maxAge(Duration.ofSeconds(cookieMaxAge))
//                .build();
//
//            // Set cookie in response
//            response.addHeader("Set-Cookie", cookie.toString());
//
//            return ResponseEntity.status(HttpStatus.OK)
//                .body(ResponseData.builder()
//                    .data(loginResponse)
//                    .desc("Login successful")
//                    .build());
//        } catch (BadRequestException ex) {
//            log.warn("Invalid account. User probably hasn't verified email.", ex);
//            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
//        }
//    }

    @GetMapping("/login")
    public ResponseEntity<?> redirectToLogin() {
        String authUrl = authService.redirectToLogin();
        return ResponseEntity.ok(Map.of("loginUrl", authUrl));  // Return URL as json
    }

    @GetMapping("/callback")
    public ResponseEntity<?> handleOAuthCallback(
        @RequestParam("code") String authCode,
        @RequestParam("state") String state,
        HttpServletResponse response
    ) {
        var loginResponse = authService.handleOAuthCallback(authCode, state, response);
        return ResponseEntity.status(HttpStatus.OK)
            .body(ResponseData.builder()
            .data(loginResponse)
            .desc("Login successful")
            .build());
    }
}
