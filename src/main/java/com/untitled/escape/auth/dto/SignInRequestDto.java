package com.untitled.escape.auth.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class SignInRequestDto {
    private String email;
    private String password;
}
