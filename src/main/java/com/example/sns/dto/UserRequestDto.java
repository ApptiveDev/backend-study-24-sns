package com.example.sns.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

// 유저를 등록할 때 필요한 정보
public record UserRequestDto(
        @NotBlank(message = "이름은 필수입니다.")
        String username,
        @NotBlank(message = "이메일은 필수입니다.")
        @Email(message = "이메일 형식이 올바르지 않습니다.")
        String email,
        @NotNull(message = "비밀번호는 필수입니다.") // null값만 막음
        @Pattern( // 빈 문자열과 공백도 막아줌
                regexp = "^(?=.*[a-zA-Z])(?=.*\\d)(?=.*[!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>\\/?]).{8,}$",
                message = "비밀번호는 영문, 숫자, 특수문자를 각각 하나 이상 포함해 총 8자 이상이어야 합니다."
        )
        String password
) {}
