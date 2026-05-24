//custom에러를 보낼 공통 예외 객체
package com.example.sns.exception;

// RuntimeException을 상속받아 원하는 ErrorCode를 만들 수 있게 함
public class CustomException extends RuntimeException {

    private final ErrorCode errorCode;

    public CustomException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }

    public ErrorCode getErrorCode() {
        return errorCode;
    }
}