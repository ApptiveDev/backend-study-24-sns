package com.example.sns.exception;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

class GlobalExceptionHandlerTest {

    private final GlobalExceptionHandler globalExceptionHandler = new GlobalExceptionHandler();

    @Test
    void handleBusiness_returnsStatusAndMessageFromErrorCode() {
        ResponseEntity<Map<String, String>> response =
                globalExceptionHandler.handleBusiness(new NotFoundException(ErrorCode.USER_NOT_FOUND));

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(response.getBody()).containsEntry("message", ErrorCode.USER_NOT_FOUND.getMessage());
    }
}
