package com.example.sns.auth;

import java.io.IOException;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class JwtAuthInterceptor implements HandlerInterceptor{
    private static final String AUTHORIZATION_HEADER = "Authorization";
    private static final String BEARER_PREFIX = "Bearer ";
    private static final String ACCESS_TOKEN_COOKIE_NAME = "ACCESS_TOKEN";

    private final JwtTokenProvider jwtTokenProvider;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws IOException {
        String token = extractToken(request);
        
        if(token == null){
            handleUnauthorized(request, response, "Access Token이 필요합니다.");
            return false;
        }

        if(!jwtTokenProvider.validateToken(token)){
            handleUnauthorized(request,response, "유효하지 않은 Access Token입니다. ");
            return  false;
        }

        Long loginUserId = jwtTokenProvider.getUserId(token);
        request.setAttribute("loginUserId", loginUserId);
        return true;
    }

    private String extractToken(HttpServletRequest request){
        String authorizationHeader = request.getHeader(AUTHORIZATION_HEADER);

        if(authorizationHeader != null && authorizationHeader.startsWith(BEARER_PREFIX)){
            return authorizationHeader.substring(BEARER_PREFIX.length());
        }

        Cookie[] cookies = request.getCookies();

        if(cookies == null){
            return null;
        }
        for(Cookie cookie : cookies){
            if(ACCESS_TOKEN_COOKIE_NAME.equals(cookie.getName())){
                return cookie.getValue();
            }
        }
        return null;
    }

    private void handleUnauthorized(HttpServletRequest request, HttpServletResponse response, String message) throws IOException{
        if(isHtmlRequest(request)){
            response.sendRedirect("/login");
            return;
        }

        writeUnauthorizedResponse(response, message);
    }

    private boolean isHtmlRequest(HttpServletRequest request){
        String acceptHeader = request.getHeader("Accept");
        return acceptHeader != null && acceptHeader.contains("text/html");
    }

    private void writeUnauthorizedResponse(HttpServletResponse response, String message) throws IOException{
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json;charset=UTF-8");
        response.getWriter().write("{\"message\":\""+message+"\"}");
    }

}
