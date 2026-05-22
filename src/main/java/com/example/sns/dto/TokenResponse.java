package com.example.sns.dto;

public record TokenResponse(
        String accessToken,
        String refreshToken
) {
}
