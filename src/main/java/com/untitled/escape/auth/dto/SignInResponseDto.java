package com.untitled.escape.auth.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Getter
@NoArgsConstructor(force = true)
public class SignInResponseDto {
    private final UUID userId;
    private final String email;
    private final String nickname;
    private final String profileUrl;

    @Builder
    public SignInResponseDto(UUID userId, String email, String nickname, String profileUrl) {
        this.userId = userId;
        this.email = email;
        this.nickname = nickname;
        this.profileUrl = profileUrl;
    }
}
