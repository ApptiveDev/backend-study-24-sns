//클라이언트에게 보낼 응답 포맷
package com.example.sns.exception;

import org.springframework.http.HttpStatus;

public record ErrorResponse(
        String status, // 예: "NOT_FOUND"
        String message // 예: "존재하지 않는 유저입니다."
) {
    public static ErrorResponse from(ErrorCode errorCode) {
        return new ErrorResponse(errorCode.getStatus().name(), errorCode.getMessage());
    }
}