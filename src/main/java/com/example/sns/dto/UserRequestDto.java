package com.example.sns.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

// 유저를 등록할 때 필요한 정보
public record UserRequestDto(
        @NotBlank(message = "이름은 필수입니다.")
        String username,
        @NotBlank(message = "이메일은 필수입니다.")
        @Email(message = "이메일 형식이 올바르지 않습니다.")
        String email,
        @NotBlank(message = "비밀번호는 필수입니다.")
        String password
) {}
