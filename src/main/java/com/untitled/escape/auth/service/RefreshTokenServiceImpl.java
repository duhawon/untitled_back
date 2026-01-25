package com.untitled.escape.auth.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Service
public class RefreshTokenServiceImpl implements RefreshTokenService {
    private static final String PREFIX = "RT:";
    private final StringRedisTemplate redisTemplate;
    @Value("${jwt.refreshTokenExpirationMs}")
    private long refreshTokenExpirationMs;

    public RefreshTokenServiceImpl(StringRedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @Override
    public void save(UUID tokenId, String refreshToken) {
        redisTemplate.opsForValue().set(
                PREFIX + tokenId,
                refreshToken,
                refreshTokenExpirationMs,
                TimeUnit.MILLISECONDS
        );
    }

    @Override
    public void delete(UUID tokenId) {
        redisTemplate.delete(PREFIX + tokenId);
    }

    @Override
    public boolean exists(UUID tokenId, String refreshToken) {
        String storedToken = redisTemplate.opsForValue().get(PREFIX + tokenId);
        return storedToken != null &&storedToken.equals(refreshToken);
    }
}
