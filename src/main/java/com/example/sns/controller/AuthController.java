package com.example.sns.controller;

import com.example.sns.auth.CustomUserDetails;
import com.example.sns.dto.LoginRequestDto;
import com.example.sns.dto.LoginResponseDto;
import com.example.sns.dto.RefreshRequestDto;
import com.example.sns.dto.TokenResponseDto;
import com.example.sns.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    // 로그인
    @PostMapping("/login")
    public ResponseEntity<LoginResponseDto> login(@RequestBody @Valid LoginRequestDto dto) {
        return ResponseEntity.ok(authService.login(dto));
    }

    // 토큰 재발급
    @PostMapping("/refresh")
    public ResponseEntity<TokenResponseDto> refresh(@RequestBody @Valid RefreshRequestDto dto) {
        return ResponseEntity.ok(authService.reissueAccessToken(dto));
    }

    // 로그아웃: Access Token 인증이 필요 (본인 확인 후 본인 Refresh Token만 삭제)
    @PostMapping("/logout")
    public ResponseEntity<Void> logout(@AuthenticationPrincipal CustomUserDetails userDetails) {
        authService.logout(userDetails.getUserId());
        return ResponseEntity.noContent().build();
    }
}
