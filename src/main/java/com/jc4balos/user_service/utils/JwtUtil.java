package com.jc4balos.user_service.utils;

import java.security.Key;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.jc4balos.user_service.model.Role;
import com.jc4balos.user_service.model.User;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

@Component
public class JwtUtil {

    @Value("${JWT.SECRET}")
    private String secret;

    @Value("${JWT.EXPIRATION}")
    private Long expiration; // in milliseconds

    public String generateToken(User user, List<Role> rolesList) {
        try {
            return Jwts.builder()
                    .subject(user.getUsername()) // was .setSubject()
                    .claim("user_uuid", user.getUserUUID())
                    .claim("roles", rolesList.stream()
                            .map(role -> Map.of(
                                    "role_uuid", role.getRoleUUID().toString(),
                                    "role_name", role.getRoleName()))
                            .toList())
                    .issuedAt(new Date()) // was .setIssuedAt()
                    .expiration(new Date(System.currentTimeMillis() + expiration)) // was .setExpiration()
                    .signWith(getSigningKey()) // no need to pass algorithm separately
                    .compact();
        } catch (Exception e) {
            e.printStackTrace();
            // Handle exceptions related to JWT generation
            throw new RuntimeException("Error generating JWT token", e);
        }

    }

    private Key getSigningKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secret); // secret should be Base64-encoded
        return Keys.hmacShaKeyFor(keyBytes);
    }
}