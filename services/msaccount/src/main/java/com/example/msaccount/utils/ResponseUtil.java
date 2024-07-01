package com.example.msaccount.utils;

import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

public class ResponseUtil {
    public static void sendUnauthorizedResponse(HttpServletResponse response, String message) throws IOException {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write("{\"error\": \"Unauthorized\", \"message\": \"Invalid or expired JWT token\"}");
    }
}
