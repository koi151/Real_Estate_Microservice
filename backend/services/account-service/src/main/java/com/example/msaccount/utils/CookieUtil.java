package com.example.msaccount.utils;

import com.example.msaccount.model.dto.CookieAttributes;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseCookie;

import java.time.Duration;

public class CookieUtil {

    public static String getCookieValue(HttpServletRequest request, String cookieName) {
        if (request.getCookies() != null) {
            for (Cookie cookie : request.getCookies()) {
                if (cookieName.equals(cookie.getName())) {
                    return cookie.getValue();
                }
            }
        }
        return null;
    }

    public void setHttpOnlyCookie(HttpServletResponse response, String accessTokenLifespan) {
        ResponseCookie cookie = ResponseCookie.from("cookieName", "cookieValue")
            .httpOnly(true)
            .secure(false)
            .sameSite("Lax")
            .path("/")
            .maxAge(Integer.parseInt(accessTokenLifespan))
            .build();

        response.addHeader("Set-Cookie", cookie.toString());
    }
}
