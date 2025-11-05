package com.line4thon.fin4u.global.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;
import java.util.Map;

@Component
public class JwtProvider {

    private final Key key;
    private final long accessExpMs;
    private final long refreshExpMs;

    public JwtProvider(
            @Value("${jwt.secret}") String secret,
            @Value("${jwt.access-exp-minutes}") long accessExpMinutes,
            @Value("${jwt.refresh-exp-days}") long refreshExpDays
    ) {
        this.key = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
        this.accessExpMs = accessExpMinutes * 60_000L;
        this.refreshExpMs = refreshExpDays * 24 * 60 * 60_000L;
    }

    public String generateAccess(String subject, Map<String, Object> claims) {
        return buildToken(subject, claims, accessExpMs);
    }
    public String generateRefresh(String subject) {
        return buildToken(subject, Map.of("typ","refresh"), refreshExpMs);
    }

    private String buildToken(String subject, Map<String, Object> claims, long ttlMs) {
        long now = System.currentTimeMillis();
        return Jwts.builder()
                .setSubject(subject)
                .addClaims(claims)
                .setIssuedAt(new Date(now))
                .setExpiration(new Date(now + ttlMs))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    public Jws<Claims> parse(String token) {
        return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
    }

    public boolean isValid(String token) {
        try {
            parse(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }

    public String getSubject(String token) {
        return parse(token).getBody().getSubject();
    }
}
