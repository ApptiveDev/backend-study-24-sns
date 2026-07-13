package com.example.sns.dto;

import jakarta.validation.constraints.NotBlank;

public record RefreshRequestDto(
        @NotBlank(message = "Refresh Token은 필수입니다.")
        String refreshToken
) {}
