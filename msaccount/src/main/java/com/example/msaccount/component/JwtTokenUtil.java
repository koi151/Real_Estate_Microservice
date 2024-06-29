package com.example.msaccount.component;

import com.example.msaccount.entity.Account;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;

import java.security.InvalidParameterException;
import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Component
@RequiredArgsConstructor
public class JwtTokenUtil {

    @Value("${JWT_EXPIRATION}")
    private int expiration;

    @Value("${JWT_SECRET_KEY}")
    private String secretKey;

    public  <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = this.extractAllClaims(token);
        return claimsResolver.apply(claims);
    }
    public String extractUserName(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    private Key getSignInKey() {
        byte[] bytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(bytes);
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSignInKey())
                .build() // Build the JwtParser
                .parseClaimsJws(token) // Parse the token
                .getBody(); // Get the claims from the parsed token
    }

    public boolean isTokenExpired(String token) {
        Date expirationDate = this.extractClaim(token, Claims::getExpiration);
        return expirationDate.before(new Date());
    }

    public boolean validateToken(String token, UserDetails userDetails) {
        String userName = extractUserName(token);
        return (userName.equals(userDetails.getUsername()))
                && !isTokenExpired(token); // check token expire time
    }

    public String generateToken(Account account) {
        // properties => claims
        Map<String, Object> claims = new HashMap<>();
        claims.put("userName", account.getAccountName());
        try {
            // token
            return Jwts.builder()
                    .setClaims(claims) // extract claims from this by Payload
                    .setSubject(account.getAccountName())
                    .setExpiration(new Date(System.currentTimeMillis() + expiration * 1000L)) // 30d
                    .signWith(getSignInKey(), SignatureAlgorithm.HS256)
                    .compact();

        }catch (Exception e) {
            throw new InvalidParameterException("Error occurred while creating JWT token: "+ e.getMessage());
        }

    }
}
