package com.example.sns.dto;

public record UserLoginResponse(
        String accessToken,
        String refreshToken
) {}