package com.untitled.escape.auth.service;

import com.untitled.escape.domain.user.User;

import java.util.UUID;

public interface RefreshTokenService {
    void save(UUID tokenId, String refreshToken);
    void delete(UUID userId);
    boolean exists(UUID tokenId, String refreshToken);
}
