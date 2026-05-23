package com.example.sns.dto;

public record SignupRequest(
        String username,
        String email,
        String password
) {
}
