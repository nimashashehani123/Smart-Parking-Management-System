package com.example.apigateway.utill;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.function.Function;

@Component
public class JwtUtil {

    @Value("${jwt.secret}")
    private String secret;
    private final long JWT_TOKEN_VALIDITY = 10 * 60 * 60 * 1000;
    public String extractUsername(String token) {
        System.out.println("JWT secret length: " + secret.length());
        String hex = HexFormat.of().formatHex(secret.getBytes(StandardCharsets.UTF_8));
        System.out.println("JWT secret (hex): " + hex);

        System.out.println("token:" + token);
        String username = extractClaim(token, Claims::getSubject);
        System.out.println("username:" + username);
        return username;
    }

    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }


    private Claims extractAllClaims(String token) {
        try {
            byte[] decodedSecret = Base64.getDecoder().decode(secret); // ✅ DECODE base64
            return Jwts.parserBuilder()
                    .setSigningKey(Keys.hmacShaKeyFor(decodedSecret))  // ✅ FIX
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
        } catch (Exception e) {
            System.out.println("❌ Error parsing token: " + e.getMessage());
            return null;
        }
    }




    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        if (claims == null) {
            // Return null or throw a custom exception, avoid NullPointerException
            return null;
        }
        return claimsResolver.apply(claims);
    }

    public boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    public Claims getUserRoleCodeFromToken(String token) {
        byte[] decodedSecret = Base64.getDecoder().decode(secret);
        return Jwts.parserBuilder()
                .setSigningKey(Keys.hmacShaKeyFor(decodedSecret))
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
    public String extractRole(String token) {
        return extractAllClaims(token).get("role", String.class);  // Should be "USER"
    }





    //validate token
    public Boolean validateToken(String token, String username) {
        final String extractedUsername = extractUsername(token);
        return (extractedUsername.equals(username) && !isTokenExpired(token));
    }
}
