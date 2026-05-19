package com.example.sns.auth.service;

import com.example.sns.auth.entity.RefreshToken;
import com.example.sns.auth.repository.RefreshTokenRepository;
import com.example.sns.auth.dto.TokenResponse;
import com.example.sns.auth.dto.LoginRequest;
import com.example.sns.auth.dto.LoginResponse;
import com.example.sns.exception.BusinessException;
import com.example.sns.exception.ErrorCode;
import com.example.sns.security.util.JwtUtil;
import com.example.sns.user.entity.User;
import com.example.sns.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;
    private final RefreshTokenRepository refreshTokenRepository;

    public LoginResponse login(LoginRequest request) {
        User user = userRepository.findByEmail(request.email())
                .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));

        if(!user.getPassword().equals(request.password())) {
            // 예외 처리
            throw new IllegalArgumentException("이메일 또는 비밀번호가 틀렸습니다.");
        }

        String accessToken = jwtUtil.generateAccessToken(user.getId());
        String refreshToken = jwtUtil.generateRefreshToken(user.getId());

        RefreshToken token = new RefreshToken(user.getId(), refreshToken);
        refreshTokenRepository.save(token);

        return LoginResponse.of(accessToken, refreshToken, user.getId());
    }

    // 검증을 RefreshToken 엔티티에서 하기
    public TokenResponse refresh(String refreshToken) {

        // 1. Refresh Token 검증
        if (!jwtUtil.validateToken(refreshToken)) {
            throw new IllegalArgumentException("유효하지 않은 Refresh Token입니다.");
        }


        // 2. DB에서 확인
        RefreshToken token = refreshTokenRepository.findByToken(refreshToken)
                .orElseThrow(() -> new IllegalArgumentException("Refresh Token을 찾을 수 없습니다."));

        // 3. 새 Access Token 발급
        String newAccessToken = jwtUtil.generateAccessToken(token.getId());

        return new TokenResponse(newAccessToken);
    }

}
