package com.example.sns.dto;

import jakarta.validation.constraints.NotBlank;

// 로그인 요청 시 사용하는 DTO다.
public record UserLoginRequest(
        @NotBlank(message = "이메일은 필수입니다.")
        String email,

        @NotBlank(message = "비밀번호는 필수입니다.")
        String password
) {}