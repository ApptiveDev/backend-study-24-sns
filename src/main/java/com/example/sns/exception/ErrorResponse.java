package com.example.sns.exception;

public record ErrorResponse(
        String code,
        String message
) {}
