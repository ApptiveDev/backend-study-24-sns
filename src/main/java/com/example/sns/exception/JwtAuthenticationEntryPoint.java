package com.example.sns.exception;

import com.example.sns.auth.JwtFilter;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {

    private final ObjectMapper objectMapper;

    @Override
    public void commence(HttpServletRequest request,
                         HttpServletResponse response,
                         AuthenticationException authException) throws IOException {

        // JwtFilter에서 만료/서명오류를 구분해서 넣어둔 값이 있으면 그걸 사용,
        // 없으면(=토큰 자체가 아예 없는 경우) 기존처럼 UNAUTHORIZED
        ErrorCode code = (ErrorCode) request.getAttribute(JwtFilter.JWT_ERROR_ATTRIBUTE);
        if (code == null) {
            code = ErrorCode.UNAUTHORIZED;
        }

        response.setStatus(code.getStatus().value());
        response.setContentType("application/json;charset=UTF-8");
        response.getWriter().write(
                objectMapper.writeValueAsString(
                        new ErrorResponse(code.getCode(), code.getMessage())
                )
        );
    }
}
