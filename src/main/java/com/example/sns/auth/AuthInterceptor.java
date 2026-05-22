package com.example.sns.auth;

import com.example.sns.exception.ErrorCode;
import com.example.sns.exception.UnauthorizedException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
@RequiredArgsConstructor
public class AuthInterceptor implements HandlerInterceptor {

    public static final String AUTH_USER_ID = "authUserId";
    private static final String BEARER_PREFIX = "Bearer ";

    private final JwtProvider jwtProvider;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        if (isPublicRequest(request)) {
            return true;
        }

        String authorization = request.getHeader(HttpHeaders.AUTHORIZATION);

        if (authorization == null || !authorization.startsWith(BEARER_PREFIX)) {
            throw new UnauthorizedException(ErrorCode.UNAUTHORIZED);
        }

        String token = authorization.substring(BEARER_PREFIX.length());
        Long userId = jwtProvider.getUserId(token);
        request.setAttribute(AUTH_USER_ID, userId);

        return true;
    }

    private boolean isPublicRequest(HttpServletRequest request) {
        String method = request.getMethod();
        String path = request.getRequestURI();

        if (HttpMethod.OPTIONS.matches(method) || HttpMethod.GET.matches(method)) {
            return true;
        }

        return HttpMethod.POST.matches(method) && "/api/users".equals(path);
    }
}
