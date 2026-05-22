package com.example.sns.dto;

public record UserCreateRequest(
        String email,
        String nickname,
        String password
) {
}