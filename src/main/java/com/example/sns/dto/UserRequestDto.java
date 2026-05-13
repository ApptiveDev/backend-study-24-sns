package com.example.sns.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

// 유저를 등록할 때 필요한 정보
public record UserRequestDto(
        @NotBlank
        String username,
        @NotBlank @Email
        String email,
        @NotBlank
        String password
) {}
