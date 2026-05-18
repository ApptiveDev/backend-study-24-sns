package com.example.sns.user.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record SignupRequest (

        @NotBlank(message = "이메일은 필수입니다.")
        @Email(message = "이메일 형식이 아닙니다.")
        String email,

        @NotBlank(message = "이름은 필수 입니다.")
        String name,

        @NotBlank(message = "비밀번호는 필수입니다.")
                @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{8,}$",
                         message = "비밀번호는 8자 이상, 영문+숫자 조합이어야 합니다.")
        String password
) {

}
