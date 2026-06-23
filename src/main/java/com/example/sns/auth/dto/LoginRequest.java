package com.example.sns.auth.dto;

public record LoginRequest(
        String email,
        String password
){
}
