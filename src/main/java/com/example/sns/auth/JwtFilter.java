package com.example.sns.auth;

import com.example.sns.exception.ErrorCode;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;

@Component
@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    private static final String HEADER_AUTHORIZATION = "Authorization";
    private static final String BEARER_PREFIX = "Bearer ";

    // EntryPoint가 어떤 에러코드로 응답할지 판단할 때 쓰는 request attribute 키
    public static final String JWT_ERROR_ATTRIBUTE = "jwtErrorCode";

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        String token = resolveToken(request); // 헤더 또는 쿠키에서 토큰 추출

        if (token != null) {
            try {
                Long userId = jwtUtil.getUserId(token); // 내부에서 파싱 + 서명검증 동시 수행

                // getUserId가 예외 없이 리턴했지만 userId가 비어있는 방어적 케이스
                // (예: subject 자체가 빈 문자열이었거나, 향후 파싱 로직 변경으로 null 리턴 가능성 대비)
                if (userId == null) {
                    request.setAttribute(JWT_ERROR_ATTRIBUTE, ErrorCode.INVALID_TOKEN);
                    filterChain.doFilter(request, response);
                    return;
                }

                CustomUserDetails userDetails = new CustomUserDetails(userId, "");
                UsernamePasswordAuthenticationToken auth =
                        new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());

                SecurityContextHolder.getContext().setAuthentication(auth);

            } catch (ExpiredJwtException e) {
                // 만료된 토큰 -> 클라이언트가 Refresh Token으로 재발급 시도하도록 구분
                request.setAttribute(JWT_ERROR_ATTRIBUTE, ErrorCode.TOKEN_EXPIRED);
            } catch (JwtException | IllegalArgumentException e) {
                // 서명 위조, 형식 오류, subject 파싱 실패 등 -> 재로그인 필요
                request.setAttribute(JWT_ERROR_ATTRIBUTE, ErrorCode.INVALID_TOKEN);
            }
        }

        filterChain.doFilter(request, response);
    }

    private String resolveToken(HttpServletRequest request) {
        // 1. Authorization 헤더에서 먼저 찾기 (API 방식)
        String authHeader = request.getHeader(HEADER_AUTHORIZATION);
        if (authHeader != null && authHeader.startsWith(BEARER_PREFIX)) {
            return authHeader.substring(BEARER_PREFIX.length());
        }

        // 2. 쿠키에서 찾기 (뷰 방식)
        if (request.getCookies() != null) {
            return Arrays.stream(request.getCookies())
                    .filter(c -> "accessToken".equals(c.getName()))
                    .map(Cookie::getValue)
                    .findFirst()
                    .orElse(null);
        }

        return null;
    }
}
