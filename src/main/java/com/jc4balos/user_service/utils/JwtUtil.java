package com.jc4balos.user_service.utils;

import java.security.Key;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

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

    public String generateToken(User user, List<String> roleKeys, List<String> roleUUIDs) {
        try {

            return Jwts.builder()
                    .subject(user.getUsername()) // keep subject for X-User (gateway uses payload.sub)
                    .claim("user_uuid", user.getUserUUID()) // gateway uses payload.user_uuid
                    .claim("full_name",
                            user.getFirstName() + " " + user.getMotherSurname() + " " + user.getFatherSurname() + " "
                                    + user.getHusbandSurname())
                    .claim("roles", toRoleObjects(roleUUIDs, roleKeys))
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

    private List<java.util.Map<String, String>> toRoleObjects(List<String> roleUUIDs, List<String> roleKeys) {
        // Build roles as: [ { "roleUUID": "...", "roleKey": "..." }, ... ]
        // Keep ordering aligned by index.
        int size = Math.min(roleUUIDs.size(), roleKeys.size());
        return java.util.stream.IntStream.range(0, size)
                .mapToObj(i -> java.util.Map.of(
                        "roleUUID", roleUUIDs.get(i),
                        "roleKey", roleKeys.get(i)))
                .toList();
    }

    private Key getSigningKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secret); // secret should be Base64-encoded
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
