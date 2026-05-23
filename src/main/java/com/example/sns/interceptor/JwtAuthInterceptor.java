package com.example.sns.interceptor;

import com.example.sns.jwt.JwtTokenProvider;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.HttpServletBean;

@Component
@RequiredArgsConstructor
public class JwtAuthInterceptor implements HandlerInterceptor {
    private final JwtTokenProvider jwtTokenProvider;

    @Override
    public boolean preHandle(
            HttpServletRequest request,
            HttpServletResponse response,
            Object handler) throws Exception {
        String authorizationHeader = request.getHeader("Authorization");

        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("Authorization header is missing or invalid");
            return false;
        }

        String token = authorizationHeader.substring(7);

        if (!jwtTokenProvider.validateToken(token)) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("Invalid or expired token");
            return false;
        }

        Long userId = jwtTokenProvider.getUserId(token);
        request.setAttribute("userId", userId);

        return true;
    }

}
