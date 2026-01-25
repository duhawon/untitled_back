package com.untitled.escape.domain.user.service;

import com.untitled.escape.domain.user.User;
import com.untitled.escape.domain.user.dto.SignUpRequestDto;

import java.util.UUID;

public interface UserService {
    User signUp(SignUpRequestDto signUpRequestDto);
    User getByEmail(String email);
    User getById(UUID userId);
}
