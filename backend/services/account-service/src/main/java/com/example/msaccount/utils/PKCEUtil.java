package com.example.msaccount.utils;

import com.example.msaccount.model.dto.CookieAttributes;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.commons.codec.digest.DigestUtils;

import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.util.Base64;

public class PKCEUtil {

    // Generate code_verifier
    public static String generateCodeVerifier() {
        SecureRandom secureRandom = new SecureRandom();
        byte[] codeVerifierBytes = new byte[64]; // Length between 43 and 128 characters
        secureRandom.nextBytes(codeVerifierBytes);
        return Base64.getUrlEncoder().withoutPadding().encodeToString(codeVerifierBytes);
    }

    // Generate code_challenge from code_verifier
    public static String generateCodeChallenge(String codeVerifier) {
        byte[] bytes = codeVerifier.getBytes(StandardCharsets.US_ASCII);
        byte[] digest = DigestUtils.sha256(bytes);
        return Base64.getUrlEncoder().withoutPadding().encodeToString(digest);
    }
}
