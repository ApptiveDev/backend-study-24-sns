package com.example.sns.exception;

import lombok.Getter;

@Getter
public class DuplicateEmailException extends RuntimeException {
    private final ErrorCode errorCode = ErrorCode.DUPLICATE_EMAIL;

    public DuplicateEmailException(String email) {
        super(ErrorCode.DUPLICATE_EMAIL.getMessage() + " email=" + email);
    }
}
