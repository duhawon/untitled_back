package com.untitled.escape.auth.jwt;

import com.untitled.escape.auth.constant.AuthConstants;
import com.untitled.escape.domain.user.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.Date;
import java.util.UUID;

@Component
public class JwtTokenProvider {
    private final SecretKey secretKey;
    @Value("${jwt.accessTokenExpirationMs}")
    private long accessTokenExpirationMs;
    @Value("${jwt.refreshTokenExpirationMs}")
    private long refreshTokenExpirationMs;

    public JwtTokenProvider(@Value("${jwt.secret}") String secret) {
        this.secretKey = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
    }

    public String createAccessToken(User user) {
        Date now = new Date();
        Date expiredDate = new Date(now.getTime() + accessTokenExpirationMs);
        return Jwts.builder()
                .subject(String.valueOf(user.getId()))
                .issuedAt(now)
                .expiration(expiredDate)
                .signWith(secretKey)
                .compact();
    }

    public String createRefreshToken(User user) {
        Date now = new Date();
        Date expiredDate = new Date(now.getTime() + refreshTokenExpirationMs);
        return Jwts.builder()
                .subject(String.valueOf(user.getId()))
                .issuedAt(now)
                .expiration(expiredDate)
                .signWith(secretKey)
                .compact();
    }

    public boolean isValidToken(String token) {
        try {
            Jwts.parser()
                    .verifyWith(secretKey)
                    .build()
                    .parseSignedClaims(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }

    public String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (bearerToken == null || !bearerToken.startsWith(AuthConstants.BEARER_PREFIX)) {
            return null;
        }
        return bearerToken.substring(7);
    }

    public Authentication getAuthentication(String token) {
        Claims claims = parseClaims(token);
        String userId = claims.getSubject();
        return new UsernamePasswordAuthenticationToken(userId,null, Collections.emptyList());
    }

    public UUID getTokenId(String refreshToken) {
        Claims claims = parseClaims(refreshToken);
        return UUID.fromString(claims.getSubject());
    }

    private Claims parseClaims(String token) {
        return Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }
}
