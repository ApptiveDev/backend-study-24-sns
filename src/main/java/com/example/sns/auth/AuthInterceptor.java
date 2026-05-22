package com.example.sns.auth;

import com.example.sns.exception.CustomException;
import com.example.sns.exception.ErrorCode;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class AuthInterceptor implements HandlerInterceptor {

    private final JwtUtil jwtUtil;

    public AuthInterceptor(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    // 컨트롤러 실행 전에 JWT 토큰을 검사하는 메서드
    @Override
    public boolean preHandle(
            HttpServletRequest request,
            HttpServletResponse response,
            Object handler
    ) {
        String authorizationHeader = request.getHeader("Authorization");

        // Authorization 헤더가 없는 경우
        if (authorizationHeader == null || authorizationHeader.isBlank()) {
            throw new CustomException(ErrorCode.TOKEN_NOT_FOUND);
        }

        // Bearer 형식이 아닌 경우
        if (!authorizationHeader.startsWith("Bearer ")) {
            throw new CustomException(ErrorCode.INVALID_TOKEN);
        }

        // "Bearer " 뒤의 실제 토큰 값만 추출
        String token = authorizationHeader.substring(7);

        // 토큰이 유효하지 않은 경우
        if (!jwtUtil.validateToken(token)) {
            throw new CustomException(ErrorCode.INVALID_TOKEN);
        }

        // 토큰에서 userId 추출
        Long userId = jwtUtil.getUserId(token);

        // 이후 컨트롤러에서 사용할 수 있도록 request에 저장
        request.setAttribute("userId", userId);

        return true;
    }
}