package com.example.sns.dto;

public record LoginRequest(
        String email,
        String password
) {
}