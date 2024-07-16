package com.example.msaccount.filters;

import com.example.msaccount.component.JwtTokenUtil;
import com.example.msaccount.entity.Account;
import com.example.msaccount.utils.ResponseUtil;
import jakarta.annotation.PostConstruct;
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
import java.util.List;

@Component
@RequiredArgsConstructor
public class JwtTokenFilter extends OncePerRequestFilter {

    @Value("${API_PREFIX}")
    private String apiPrefix;

    private final UserDetailsService userDetailsService;
    private final JwtTokenUtil jwtTokenUtil;
    private List<Pair<String, String>> BYPASS_TOKENS;
    @PostConstruct
    public void init() {
        BYPASS_TOKENS = List.of(
                Pair.of(String.format("%s/property", apiPrefix), "GET"),
                Pair.of(String.format("%s/property-category", apiPrefix), "GET"),
                Pair.of(String.format("%s/accounts/register", apiPrefix), "POST"),
                Pair.of(String.format("%s/admin/accounts/login", apiPrefix), "POST")

        );
    }


    // Bypass the filter if the request matches bypass criteria
    private boolean isBypassToken(@NonNull HttpServletRequest request) { // stream api
//        return BYPASS_TOKENS.stream().anyMatch(
//                pair -> request.getServletPath().contains(pair.getFirst()) && // if satisfied -> return true
//                        request.getMethod().equals(pair.getSecond())
//        );
        return true; // testing
    }

    //  The method intercepts each HTTP request to apply security checks or transformations.
    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request,
                                    @NonNull HttpServletResponse response,
                                    @NonNull FilterChain filterChain)
            throws IOException, ServletException {
        try {

            if (isBypassToken(request)) {
                filterChain.doFilter(request, response); // Bypass the filter if the request matches bypass criteria
                return;
            }

            final String authHeader = request.getHeader("Authorization");
            if (authHeader == null || !authHeader.startsWith("Bearer ")) { // header check
                ResponseUtil.sendUnauthorizedResponse(response, "Authorization header is missing or invalid");
                return;
            }

            final String token = authHeader.substring(7);
            final String userName = jwtTokenUtil.extractUserName(token);

            if (userName != null && SecurityContextHolder.getContext().getAuthentication() == null) { // check if user exists and no user authenticated in SecurityContextHolder
                Account userDetails = (Account) userDetailsService.loadUserByUsername(userName); // service from java.security lib

                // check the validity of the token, check if it's the right person and token not expired
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
            System.out.println("Error occurred in doFilterInternal method: " + e.getMessage());
            ResponseUtil.sendUnauthorizedResponse(response,"Invalid or expired JWT token");
        }
    }
}
