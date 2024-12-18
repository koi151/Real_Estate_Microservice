package com.example.msaccount.service.impl;

import com.example.msaccount.customExceptions.*;
import com.example.msaccount.model.dto.AccountDetailDTO;
import com.example.msaccount.model.dto.TokenResponseDTO;
import com.example.msaccount.service.AuthService;
import com.example.msaccount.utils.CookieUtil;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import jakarta.servlet.http.HttpServletRequest;
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
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.ParseException;
import java.time.Duration;
import java.time.Instant;
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

    @Value("${KEYCLOAK_TOKEN_URI}")
    private String tokenUri;

    @Value("${KEYCLOAK_REDIRECT_URI}")
    private String redirectUri;

    @Value("${KEYCLOAK_AUTHORIZATION_URL}")
    private String authorizationUrl;

    @Value("${KEYCLOAK_REALM}")
    private String realm;

    @Value("${KEYCLOAK_ADMIN_HOME_URL}")
    private String adminHomeUrl;

    @Value("${KEYCLOAK_TOKEN_URI}")
    private String keycloakTokenUrl;

    @Value("${KEYCLOAK_REFRESH_TOKEN_LIFESPAN}")
    private String refreshTokenLifespan;

    @Value("${KEYCLOAK_ACCESS_TOKEN_LIFESPAN}")
    private String accessTokenLifespan;

    private final RestTemplate restTemplate;
    private final RedisTemplate<String, Object> redisTemplate;
    private final KeycloakUserService kcUserService;


    @Override
    public String redirectToLogin(String codeChallenge,String codeChallengeMethod,String state) {

        redisTemplate.opsForValue().set("pkce:state:" + state, state, 10, TimeUnit.MINUTES);
        redisTemplate.opsForValue().set("pkce:challenge:" + state, codeChallenge, 10, TimeUnit.MINUTES);

        // Xây dựng URL login Keycloak
        return String.format(
            "%s/realms/%s/protocol/openid-connect/auth" +
                "?response_type=code" +
                "&client_id=%s" +
                "&redirect_uri=%s" +
                "&code_challenge=%s" +
                "&code_challenge_method=%s" +
                "&state=%s",
            authorizationUrl,
            realm,
            URLEncoder.encode(clientId, StandardCharsets.UTF_8),
            URLEncoder.encode(redirectUri, StandardCharsets.UTF_8),
            URLEncoder.encode(codeChallenge, StandardCharsets.UTF_8),
            URLEncoder.encode(codeChallengeMethod, StandardCharsets.UTF_8),
            URLEncoder.encode(state, StandardCharsets.UTF_8)
        );
    }

    // compute code_challenge from code_verifier
    private String computeCodeChallenge(String codeVerifier) {
        try {
            // use SHA-256 algo to hash code_verifier
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(codeVerifier.getBytes(StandardCharsets.UTF_8));

            // convert to Base64 URL
            return Base64.getUrlEncoder().withoutPadding().encodeToString(hash);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Error computing code challenge", e);
        }
    }


//    @Override
//    public LoginResponseDTO handleOAuthCallback(String authCode, String state, HttpServletResponse response) {
//        try {
//            String codeVerifier = (String) redisTemplate.opsForValue().get("pkce:" + state);
//
//            if (codeVerifier == null) {
//                log.error("Invalid code_verifier - value: " + codeVerifier);
//                throw new KeycloakLoginFailedException("Invalid or expired code_verifier");
//            }
//
//            String tokenResponse = exchangeCodeForToken(authCode, codeVerifier);
//
//            // Parse JSON response
//            JsonNode tokenJson = objectMapper.readTree(tokenResponse);
//            if (tokenJson.has("error")) {
//                throw new KeycloakLoginFailedException("Error occurred while parsing token");
//            }
//
//            String accessToken = tokenJson.get("access_token").asText();
//            String refreshToken = tokenJson.get("refresh_token").asText();
//
//            CookieUtil.addCookie(response, CookieAttributes.builder()
//                .name("refresh_token")
//                .value(refreshToken)
//                .maxAge(Integer.parseInt(refreshTokenLifespan))
//                .build());
//
//            response.addCookie(new Cookie("refresh_token", refreshToken));
//
//
//            AccountDetailDTO accountInfo = extractUserInfoFromAccessToken(accessToken);
//            response.sendRedirect(adminHomeUrl);
//
//            return LoginResponseDTO.builder()
//                .accountInfo(accountInfo)
//                .tokenResponses(  TokenResponseDTO.builder()
//                    .accessToken(accessToken)
//                    .refreshToken(refreshToken)
//                    .expiresIn(tokenJson.get("expires_in").asInt()) // ttl access token
//                    .build())
//                .build();
//
//        } catch (Exception e) {
//            log.error("Error processing authentication: " + e.getMessage());
//            throw new KeycloakLoginFailedException("Error processing authentication");
//        }
//    }

    @Override
    public TokenResponseDTO refreshToken(HttpServletRequest request) {
        try {
            String refreshToken = CookieUtil.getCookieValue(request, "refresh_token");

            if (refreshToken == null || refreshToken.isEmpty()) {
                throw new InvalidTokenException("Refresh token is missing or invalid");
            }

            String newAccessToken = kcUserService.refreshAccessToken(refreshToken);
            JsonNode tokenJson = new ObjectMapper().readTree(newAccessToken);
            int expiresIn = tokenJson.get("expires_in").asInt();

            if (newAccessToken == null || newAccessToken.isEmpty()) {
                throw new InvalidTokenException("Failed to retrieve a new access token");
            }

            return TokenResponseDTO.builder()
                .accessToken(newAccessToken)
                .expiresIn(expiresIn)
                .build();

        } catch (InvalidTokenException ex) {
            log.error("Token validation error: {}", ex.getMessage());
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, ex.getMessage());

        } catch (Exception ex) {
            log.error("Unexpected error during token refresh: {}", ex.getMessage());
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Unable to refresh access token");
        }
    }

    @Override
    public void exchangeToken(MultiValueMap<String, String> requestBody, HttpServletResponse httpServletResponse) {
        String code = requestBody.getFirst("code");
        String state = requestBody.getFirst("state");
        String codeVerifier = requestBody.getFirst("code_verifier");

        if (code == null || state == null || codeVerifier == null) {
            throw new IllegalArgumentException("Missing required parameters.");
        }

        // check state from Redis
        String storedState = (String) redisTemplate.opsForValue().get("pkce:state:" + state);
        if (storedState == null || !storedState.equals(state)) {
            throw new SecurityException("Invalid state or session expired.");
        }

        String storedCodeChallenge = (String) redisTemplate.opsForValue().get("pkce:challenge:" + state);
        if (storedCodeChallenge == null) {
            throw new SecurityException("Code challenge is missing or expired.");
        }

        // compare code_verifier & code_challenge
        String computedCodeChallenge = computeCodeChallenge(codeVerifier);
        if (!computedCodeChallenge.equals(storedCodeChallenge)) {
            throw new SecurityException("Code verifier does not match the code challenge.");
        }

        redisTemplate.delete("pkce:state:" + state);
        redisTemplate.delete("pkce:challenge:" + state);

        // Exchange token with Keycloak
        MultiValueMap<String, String> tokenRequest = new LinkedMultiValueMap<>();
        tokenRequest.add("grant_type", "authorization_code");
        tokenRequest.add("code", code);
        tokenRequest.add("redirect_uri", redirectUri);
        tokenRequest.add("client_id", clientId);
        tokenRequest.add("code_verifier", codeVerifier);
        tokenRequest.add("client_secret", clientSecret);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(tokenRequest, headers);

        try {
            // Send request to Keycloak to exchange token
            ResponseEntity<Map> response = restTemplate.postForEntity(keycloakTokenUrl, requestEntity, Map.class);

            // Parse the token response
            Map<String, Object> tokenResponse = response.getBody();
            if (tokenResponse == null || !tokenResponse.containsKey("access_token")) {
                throw new RuntimeException("Invalid response from Keycloak during token exchange.");
            }

            String accessToken = (String) tokenResponse.get("access_token");
            String refreshToken = (String) tokenResponse.get("refresh_token");

            // Save refresh token in Redis with expiration (match the expiration of the refresh token)
            if (refreshToken != null) {
                long refreshTokenExpiry = parseTokenExpiry(refreshToken);
                redisTemplate.opsForValue().set("refresh_token", refreshToken, refreshTokenExpiry, TimeUnit.SECONDS);
            }

            ResponseCookie cookie = ResponseCookie.from("access_token", accessToken)
                .httpOnly(true)
                .secure(true)
                .path("/")
                .value(accessToken)
                .maxAge(300)
                .build();

            redisTemplate.opsForValue().set("access_token", accessToken, Long.parseLong(accessTokenLifespan), TimeUnit.SECONDS);

            log.info("Set cookie: {}", cookie);
            httpServletResponse.addHeader("Set-Cookie", cookie.toString());
            log.info("Headers after set: {}", Arrays.toString(httpServletResponse.getHeaderNames().toArray()));


        } catch (HttpClientErrorException ex) {
            log.error("Error during token exchange: " + ex.getMessage());
            throw new KeycloakTokenExchangeException("Error during token exchange: " + ex.getCause());
        }
    }

    private long parseTokenExpiry(String refreshToken) {
        try {
            String[] tokenParts = refreshToken.split("\\.");
            if (tokenParts.length < 2) {
                throw new IllegalArgumentException("Invalid refresh token format.");
            }

            String payload = new String(Base64.getDecoder().decode(tokenParts[1]), StandardCharsets.UTF_8);
            Map payloadMap = new ObjectMapper().readValue(payload, Map.class);

            long exp = ((Number) payloadMap.get("exp")).longValue();
            long currentTime = Instant.now().getEpochSecond();

            // calculate ttl in seconds
            return exp - currentTime;

        } catch (Exception e) {
            log.error("Failed to parse refresh token expiration: " + e.getMessage());
            throw new TokenParsingException("Failed to parse refresh token expiration.", e);
        }
    }




    private String exchangeCodeForToken(String code, String codeVerifier) throws Exception {
        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            HttpPost post = new HttpPost(tokenUri);

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
                .roleNames((List<String>) roleNames)
                .email(claims.getStringClaim("email"))
                .emailVerified(claims.getBooleanClaim("email_verified"))
                .scopes(scopes)
//                .isAdmin(kcUserService.isAdminUser(userId))
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
