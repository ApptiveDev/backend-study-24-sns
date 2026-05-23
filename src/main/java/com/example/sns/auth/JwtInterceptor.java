package com.example.sns.auth;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
@RequiredArgsConstructor
public class JwtInterceptor implements HandlerInterceptor {

    private final JwtUtil jwtUtil;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        // 1. Authorization 헤더에서 토큰 꺼내기
        String authHeader = request.getHeader("Authorization");

        // 2. 토큰 없거나 형식이 잘못된 경우 401 반환
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "토큰이 없습니다.");
            return false;
        }

        // 3. "Bearer " 이후의 실제 토큰 값만 추출
        String token = authHeader.substring(7);

        // 4. 토큰 유효성 검증
        if (!jwtUtil.validateToken(token)) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "유효하지 않은 토큰입니다.");
            return false;
        }

        // 5. 토큰에서 userId 꺼내서 request에 저장
        Long userId = jwtUtil.getUserId(token);
        request.setAttribute("userId", userId);

        return true; // 통과
    }
}
