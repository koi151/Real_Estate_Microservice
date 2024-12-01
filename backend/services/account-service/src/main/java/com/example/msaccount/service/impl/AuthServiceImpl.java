package com.example.msaccount.service.impl;

import com.example.msaccount.customExceptions.InvalidAuthorizationHeaderException;
import com.example.msaccount.customExceptions.KeycloakLoginFailedException;
import com.example.msaccount.customExceptions.TokenParsingException;
import com.example.msaccount.model.dto.AccountDetailDTO;
import com.example.msaccount.model.dto.CookieAttributes;
import com.example.msaccount.model.request.LoginRequest;
import com.example.msaccount.model.response.LoginResponse;
import com.example.msaccount.model.response.UserInfoResponse;
import com.example.msaccount.service.AuthService;
import com.example.msaccount.service.KeycloakUserService;
import com.example.msaccount.utils.PKCEUtil;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.Charsets;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.keycloak.representations.AccessTokenResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.ParseException;
import java.time.Duration;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
@Slf4j
public class AuthServiceImpl implements AuthService {

    @Value("${USER_CACHE_TTL}")
    private long userCacheTtl;

    @Value(("${KEYCLOAK_CLIENT_SECRET}"))
    private String clientSecret;

    @Value(("${KEYCLOAK_CLIENT_ID}"))
    private String clientId;

    @Value("${KEYCLOAK_TOKEN_URL}")
    private String tokenUrl;

    @Value("${KEYCLOAK_REDIRECT_URI}")
    private String redirectUri;

    @Value("${KEYCLOAK_AUTHORIZATION_URL}")
    private String authorizationUrl;

    @Value("${KEYCLOAK_REALM}")
    private String realm;

    @Value("${KEYCLOAK_ADMIN_HOME_URL}")
    private String adminHomeUrl;

    @Value("${KEYCLOAK_ACCESS_TOKEN_LIFESPAN}")
    private String accessTokenLifespan;

    @Value("${KEYCLOAK_REFRESH_TOKEN_LIFESPAN}")
    private String refreshTokenLifespan;

    private final ObjectMapper objectMapper;
    private final RedisTemplate<String, Object> redisTemplate;
    private final KeycloakUserService kcUserService;

//    @Override
//    public LoginResponse login(LoginRequest request) {
//        AccessTokenResponse tokenResponse = kcUserService.getAccessToken(request.username(), request.password());
//        AccountDetailDTO accDTO = extractUserInfoFromAccessToken(tokenResponse.getToken());
//        cacheUserData(accDTO);
//        cacheRefreshToken(
//            tokenResponse.getRefreshToken(),
//            accDTO.getAccountId(),
//            Duration.ofSeconds(tokenResponse.getRefreshExpiresIn())
//        );
//
//        UserInfoResponse userInfoResponse = UserInfoResponse.builder()
//            .username(accDTO.getUsername())
//            .email(accDTO.getEmail())
//            .fullName(accDTO.getFirstName() + " " + accDTO.getLastName())
//            .build();
//
//        return LoginResponse.builder()
//            .accessToken(tokenResponse)
//            .userInfo(userInfoResponse)
//            .build();
//    }

    @Override
    public String redirectToLogin() {
        String codeVerifier = PKCEUtil.generateCodeVerifier();
        String codeChallenge = PKCEUtil.generateCodeChallenge(codeVerifier);

        // Generate a random state
        String state = UUID.randomUUID().toString();

        redisTemplate.opsForValue().set("pkce:" + state, codeVerifier, 10, TimeUnit.MINUTES);

        // Build Keycloak authorization URL
        return String.format(
            "%s/realms/%s/protocol/openid-connect/auth" +
                "?response_type=code" +
                "&client_id=%s" +
                "&redirect_uri=%s" +
                "&code_challenge=%s" +
                "&code_challenge_method=S256" +
                "&state=%s",
            authorizationUrl,
            realm,
            URLEncoder.encode(clientId, StandardCharsets.UTF_8),
            URLEncoder.encode(redirectUri, StandardCharsets.UTF_8),
            URLEncoder.encode(codeChallenge, StandardCharsets.UTF_8),
            URLEncoder.encode(state, StandardCharsets.UTF_8)
        );
    }

    @Override
    public AccountDetailDTO handleOAuthCallback(String authCode, String state, HttpServletResponse response) {
        try {
            String codeVerifier = (String) redisTemplate.opsForValue().get("pkce:" + state);

            if (codeVerifier == null) {
                log.error("Invalid code_verifier - value: " + codeVerifier);
                throw new KeycloakLoginFailedException("Invalid or expired code_verifier");
            }

            String tokenResponse = exchangeCodeForToken(authCode, codeVerifier);

            // Parse JSON response
            JsonNode tokenJson = objectMapper.readTree(tokenResponse);
            if (tokenJson.has("error")) {
                // error -> redirect back to login page
                response.sendRedirect("/api/auth/login");
                throw new KeycloakLoginFailedException("Error occurred while parsing token");
            }

            String accessToken = tokenJson.get("access_token").asText();
            String refreshToken = tokenJson.get("refresh_token").asText();
            storeHttpOnlyCookies(accessToken, refreshToken, response);

            response.sendRedirect(adminHomeUrl);
            return extractUserInfoFromAccessToken(accessToken);

        } catch (Exception e) {
            log.error("Error processing authentication: " + e.getMessage());
            throw new KeycloakLoginFailedException("Error processing authentication");
        }
    }

    private String exchangeCodeForToken(String code, String codeVerifier) throws Exception {
        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            HttpPost post = new HttpPost(tokenUrl);

            // req parameters
            List<BasicNameValuePair> params = new ArrayList<>();
            params.add(new BasicNameValuePair("grant_type", "authorization_code"));
            params.add(new BasicNameValuePair("client_id", clientId));
            params.add(new BasicNameValuePair("code", code));
            params.add(new BasicNameValuePair("redirect_uri", redirectUri));
            params.add(new BasicNameValuePair("code_verifier", codeVerifier));

            // Client authentication is enabled -> include client_secret
            if (clientSecret != null && !clientSecret.isEmpty()) {
                params.add(new BasicNameValuePair("client_secret", clientSecret));
            }

            post.setEntity(new UrlEncodedFormEntity(params, Charsets.UTF_8));
            post.setHeader("Content-Type", "application/x-www-form-urlencoded");

            CloseableHttpResponse tokenResponse = httpClient.execute(post);
            return EntityUtils.toString(tokenResponse.getEntity());
        }
    }

    private void storeHttpOnlyCookies(String accessToken, String refreshToken, HttpServletResponse response){
        log.debug(accessTokenLifespan);

        addCookie(response, CookieAttributes.builder()
            .name("access_token")
            .value(accessToken)
            .maxAge(Integer.parseInt(accessTokenLifespan))
            .build());

        addCookie(response, CookieAttributes.builder()
            .name("refresh_token")
            .value(refreshToken)
            .maxAge(Integer.parseInt(refreshTokenLifespan))
            .build());
    }

    private void addCookie(HttpServletResponse response, CookieAttributes attributes) {
        Cookie cookie = new Cookie(attributes.name(), attributes.value());
        cookie.setHttpOnly(true);
        cookie.setSecure(true);
        cookie.setPath("/");
        cookie.setMaxAge(attributes.maxAge());
        response.addCookie(cookie);
    }

    private Set<String> extractRolesFromResourceAccess(JWTClaimsSet claims) {
        Map<String, Object> resourceAccess = (Map<String, Object>) claims.getClaim("resource_access");
        Set<String> allRoles = new HashSet<>();

        for (Map.Entry<String, Object> entry : resourceAccess.entrySet()) {
            if (entry.getValue() instanceof Map) {
                Map<String, Object> serviceAccess = (Map<String, Object>) entry.getValue();

                Object rolesObj = serviceAccess.get("roles");
                if (rolesObj instanceof List) {
                    List<String> roles = (List<String>) rolesObj;
                    allRoles.addAll(roles);
                }
            }
        }
        return allRoles;
    }

    public AccountDetailDTO extractUserInfoFromAccessToken(String accessToken) {
        try {
            SignedJWT signedJWT = SignedJWT.parse(accessToken);
            JWTClaimsSet claims = signedJWT.getJWTClaimsSet();

            String userId = claims.getSubject();
            Set<String> scopes = Arrays.stream(claims.getStringClaim("scope")
                    .split(" "))
                .collect(Collectors.toSet());
            Set<String> roleNames = extractRolesFromResourceAccess(claims);

            return AccountDetailDTO.builder()
                .accountId(userId)
                .username(claims.getStringClaim("preferred_username"))
                .firstName(claims.getStringClaim("firstName"))
                .lastName(claims.getStringClaim("lastName"))
                .roleNames(roleNames)
                .email(claims.getStringClaim("email"))
                .emailVerified(claims.getBooleanClaim("email_verified"))
                .scopes(scopes)
                .isAdmin(kcUserService.isAdminUser(userId))
                .build();

        } catch (ParseException ex) {
            throw new TokenParsingException("Failed to parse access token", ex);
        }
    }

    public String extractAccessTokenFromAuthHeader(String authorizationHeader) {
        return authorizationHeader.substring(7);
    }

    public String extractUserIDFromAuthHeader(String authorizationHeader) {
        String accessToken = extractAccessTokenFromAuthHeader(authorizationHeader);
        JWTClaimsSet claims = getJWTClaimsSet(accessToken);
        return claims.getSubject();
    }

    public JWTClaimsSet getJWTClaimsSet(String accessToken) {
        try {
            SignedJWT signedJWT = SignedJWT.parse(accessToken);
            return signedJWT.getJWTClaimsSet();
        } catch (ParseException ex) {
            throw new TokenParsingException("Failed to parse access token", ex);
        }
    }



    private void cacheUserData(AccountDetailDTO accDTO) {
        String key = "userId:" + accDTO.getAccountId();
        redisTemplate.opsForValue().set(key, accDTO, Duration.ofSeconds(userCacheTtl));
    }

    private void cacheRefreshToken(String refreshToken, String userId, Duration ttl) {
        String key = "refresh_token:" + refreshToken;
        redisTemplate.opsForValue().set(key, userId, ttl);
    }

    public static String extractUserIdFromToken(String authorizationHeader) {
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            String accessToken = authorizationHeader.substring(7);
            try {
                SignedJWT signedJWT = SignedJWT.parse(accessToken);
                JWTClaimsSet claims = signedJWT.getJWTClaimsSet();
                return claims.getSubject();

            } catch (ParseException ex) {
                throw new TokenParsingException("Failed to parse access token", ex);
            }
        }
        throw new InvalidAuthorizationHeaderException("Invalid Authorization header");
    }
}
