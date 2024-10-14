//package com.example.msaccount.component;
//
//import com.example.msaccount.entity.Account;
//import io.jsonwebtoken.Jwts;
//import io.jsonwebtoken.SignatureAlgorithm;
//import io.jsonwebtoken.io.Decoders;
//import io.jsonwebtoken.security.Keys;
//import lombok.RequiredArgsConstructor;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.stereotype.Component;
//
//import io.jsonwebtoken.Claims;
//
//import java.security.InvalidParameterException;
//import java.security.Key;
//import java.util.Date;
//import java.util.HashMap;
//import java.util.Map;
//import java.util.function.Function;
//
//@Component
//@RequiredArgsConstructor
//public class JwtTokenUtil {
//
//    @Value("${JWT_EXPIRATION}")
//    private int expiration;
//
//    @Value("${JWT_SECRET_KEY}")
//    private String secretKey;
//
//    // This method is used to extract specific information from the token.
//    public  <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
//        final Claims claims = this.extractAllClaims(token);
//        return claimsResolver.apply(claims);
//    }
//
//    public String extractUserName(String token) {
//        return extractClaim(token, Claims::getSubject);
//    }
//
//    // This method creates a secret key (Key) from a Base64 encoded secret key string (secretKey).
//    private Key getSignInKey() {
//        byte[] bytes = Decoders.BASE64.decode(secretKey);
//        return Keys.hmacShaKeyFor(bytes);
//    }
//
//    // method used to extract all information (claims) from token
//    private Claims extractAllClaims(String token) {
//        return Jwts.parserBuilder()
//                .setSigningKey(getSignInKey()) // It uses the secret key to decrypt the token and retrieve the information
//                .build() // Build the JwtParser
//                .parseClaimsJws(token) // Parse the token
//                .getBody(); // Get the claims from the parsed token
//    }
//
//    // check if token expired or not
//    public boolean isTokenExpired(String token) {
//        Date expirationDate = this.extractClaim(token, Claims::getExpiration);
//        return expirationDate.before(new Date());
//    }
//
//    // check validity, checks that the username in the token matches the username in UserDetails and that the token has not expired.
//    public boolean validateToken(String token, UserDetails userDetails) {
//        String userName = extractUserName(token);
//        return (userName.equals(userDetails.getUsername()))
//                && !isTokenExpired(token);
//    }
//
//    // create new token, It puts information into the token (claims)
//    public String generateToken(Account account) {
//        // properties => claims
//        Map<String, Object> claims = new HashMap<>();
//        claims.put("userName", account.getAccountName()); // puts username.
//        try {
//            return Jwts.builder()
//                    .setClaims(claims) // extract claims from this by Payload
//                    .setSubject(account.getAccountName())
//                    .setExpiration(new Date(System.currentTimeMillis() + expiration * 1000L)) // convert seconds to milliseconds
//                    .signWith(getSignInKey(), SignatureAlgorithm.HS256) // token is signed using a private key and HS256 algorithm.
//                    .compact();
//
//        }catch (Exception e) {
//            throw new InvalidParameterException("Error occurred while creating JWT token: "+ e.getMessage());
//        }
//    }
//}
