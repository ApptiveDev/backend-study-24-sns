package com.example.sns.auth.dto;

// Refresh 요청
public record RefreshRequest(
        String refreshToken
) {
}
