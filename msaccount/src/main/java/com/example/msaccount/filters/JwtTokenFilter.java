package com.example.msaccount.filters;

import com.example.msaccount.component.JwtTokenUtil;
import com.example.msaccount.entity.Account;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.util.Pair;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@Component
@RequiredArgsConstructor
public class JwtTokenFilter extends OncePerRequestFilter {

    @Value("${API_PREFIX}")
    private String apiPrefix;

    private final UserDetailsService userDetailsService;
    private final JwtTokenUtil jwtTokenUtil;

    //  The method intercepts each HTTP request to apply security checks or transformations.
    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request,
                                    @NonNull HttpServletResponse response,
                                    @NonNull FilterChain filterChain)
            throws IOException {
        try {

            if (isBypassToken(request)) {
                filterChain.doFilter(request, response); // Bypass the filter if the request matches bypass criteria
                return;
            }

            final String authHeader = request.getHeader("Authorization");
            if (authHeader == null || !authHeader.startsWith("Bearer ")) { // header check
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized");
                return;
            }

            final String token = authHeader.substring(7);
            final String userName = jwtTokenUtil.extractUserName(token);

            if (userName != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                Account userDetails = (Account) userDetailsService.loadUserByUsername(userName); // service from java.security lib

                if (jwtTokenUtil.validateToken(token, userDetails)) {
                    UsernamePasswordAuthenticationToken authenticationToken =
                            new UsernamePasswordAuthenticationToken(userDetails, null,
                                    userDetails.getAuthorities());
                    authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                }
            }
            filterChain.doFilter(request, response); // enable bypass

        } catch (Exception e) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized");
        }
    }

    // Bypass the filter if the request matches bypass criteria
    private boolean isBypassToken(@NonNull  HttpServletRequest request) {
        final List<Pair<String, String>> bypassTokens = Arrays.asList(
                Pair.of(String.format("%s/property", apiPrefix), "GET"),
                Pair.of(String.format("%s/property-category", apiPrefix), "GET"),
                Pair.of(String.format("%s/account/register", apiPrefix), "POST"),
                Pair.of(String.format("%s/account/login", apiPrefix), "POST"),
                Pair.of(String.format("%s/admin/account/", apiPrefix), "POST") // tempo
        );

        for (Pair<String, String> bypassToken: bypassTokens) {
            if (request.getServletPath().contains(bypassToken.getFirst()) &&
                    request.getMethod().equals(bypassToken.getSecond())) {
                return true;
            }
        }

        return false;
    }
}
