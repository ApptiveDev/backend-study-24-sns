package com.example.sns.controller;

import com.example.sns.dto.LoginRequest;
import com.example.sns.dto.LoginResponse;
import com.example.sns.dto.TokenReissueRequest;
import com.example.sns.service.AuthService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    // 로그인
    @PostMapping("/login")
    public LoginResponse login(@RequestBody LoginRequest request) {
        return authService.login(request);
    }

    // Access Token 재발급
    @PostMapping("/reissue")
    public LoginResponse reissueAccessToken(@RequestBody TokenReissueRequest request) {
        return authService.reissueAccessToken(request);
    }
}