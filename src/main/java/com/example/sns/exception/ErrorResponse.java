package com.example.sns.exception;

import java.time.LocalDateTime;

public record ErrorResponse(
        String errorCode,
        int status,
        String message,
        LocalDateTime timestamp
) {
    public static ErrorResponse of(ErrorCode errorCode, String customMessage) {
        return new ErrorResponse(
                errorCode.name(),
                errorCode.getStatus().value(),
                customMessage,
                LocalDateTime.now()
        );
    }
}