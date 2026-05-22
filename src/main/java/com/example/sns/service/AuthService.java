package com.example.sns.service;

import com.example.sns.auth.JwtProvider;
import com.example.sns.dto.AuthLoginRequest;
import com.example.sns.dto.RefreshTokenRequest;
import com.example.sns.dto.TokenResponse;
import com.example.sns.entity.RefreshToken;
import com.example.sns.entity.User;
import com.example.sns.exception.ErrorCode;
import com.example.sns.exception.NotFoundException;
import com.example.sns.exception.UnauthorizedException;
import com.example.sns.repository.RefreshTokenRepository;
import com.example.sns.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.Base64;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AuthService {

    private static final SecureRandom SECURE_RANDOM = new SecureRandom();

    private final UserRepository userRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final JwtProvider jwtProvider;

    @Value("${jwt.refresh-token-expiration-days}")
    private long refreshTokenExpirationDays;

    @Transactional
    public TokenResponse login(AuthLoginRequest request) {
        User user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new NotFoundException(ErrorCode.USER_NOT_FOUND));

        refreshTokenRepository.deleteByUser(user);

        String accessToken = jwtProvider.createAccessToken(user);
        String refreshToken = createRefreshToken();

        refreshTokenRepository.save(new RefreshToken(
                user,
                refreshToken,
                LocalDateTime.now().plusDays(refreshTokenExpirationDays)
        ));

        return new TokenResponse(accessToken, refreshToken);
    }

    @Transactional
    public TokenResponse refresh(RefreshTokenRequest request) {
        RefreshToken refreshToken = refreshTokenRepository.findByToken(request.getRefreshToken())
                .orElseThrow(() -> new UnauthorizedException(ErrorCode.REFRESH_TOKEN_NOT_FOUND));

        if (refreshToken.isExpired(LocalDateTime.now())) {
            refreshTokenRepository.delete(refreshToken);
            throw new UnauthorizedException(ErrorCode.REFRESH_TOKEN_EXPIRED);
        }

        String accessToken = jwtProvider.createAccessToken(refreshToken.getUser());
        return new TokenResponse(accessToken, refreshToken.getToken());
    }

    @Transactional
    public void logout(RefreshTokenRequest request) {
        RefreshToken refreshToken = refreshTokenRepository.findByToken(request.getRefreshToken())
                .orElseThrow(() -> new UnauthorizedException(ErrorCode.REFRESH_TOKEN_NOT_FOUND));

        refreshTokenRepository.delete(refreshToken);
    }

    private String createRefreshToken() {
        byte[] randomBytes = new byte[32];
        SECURE_RANDOM.nextBytes(randomBytes);
        return Base64.getUrlEncoder().withoutPadding().encodeToString(randomBytes);
    }
}
