package com.helpdesk.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

// Utility component responsible for generating, parsing, and verifying JWT tokens
@Component
public class JwtUtil {

    // Generates a secure HMAC-SHA256 key for signing and verifying tokens
    private final Key key = Keys.secretKeyFor(SignatureAlgorithm.HS256);

    // Configures token expiration period to 24 hours in milliseconds
    private final long EXPIRE_DURATION = 24 * 60 * 60 * 1000;

    // Generates a signed JWT token incorporating user email and role claim
    public String generateToken(String email, String role) {
        return Jwts.builder()
                .setSubject(email)
                .claim("role", role)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRE_DURATION))
                .signWith(key)
                .compact();
    }

    // Extracts subject (user email) from token claims
    public String extractUsername(String token) {
        return extractAllClaims(token).getSubject();
    }

    // Helper method to parse token payload using configured signing key
    private Claims extractAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    // Validates token signature and checks if token is expired
    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
