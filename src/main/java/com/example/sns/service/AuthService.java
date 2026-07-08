package com.example.sns.service;

import com.example.sns.auth.JwtUtil;
import com.example.sns.dto.LoginRequestDto;
import com.example.sns.dto.LoginResponseDto;
import com.example.sns.dto.RefreshRequestDto;
import com.example.sns.dto.TokenResponseDto;
import com.example.sns.entity.RefreshToken;
import com.example.sns.entity.User;
import com.example.sns.exception.InvalidLoginException;
import com.example.sns.exception.RefreshTokenExpiredException;
import com.example.sns.exception.RefreshTokenNotFoundException;
import com.example.sns.exception.UserNotFoundException;
import com.example.sns.repository.RefreshTokenRepository;
import com.example.sns.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AuthService {

    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;
    private final RefreshTokenRepository refreshTokenRepository;
    private final PasswordEncoder passwordEncoder;

    // 로그인
    @Transactional
    public LoginResponseDto login(LoginRequestDto dto) {
        // 1. 이메일로 유저 찾기
        User user = userRepository.findByEmail(dto.email())
                .orElseThrow(InvalidLoginException::new);

        // 2. 비밀번호 확인
        if (!passwordEncoder.matches(dto.password(), user.getPassword())) {
            throw new InvalidLoginException();
        }

        // 3. JWT 발급
        String accessToken = jwtUtil.generateAccessToken(user.getId());
        String refreshToken = jwtUtil.generateRefreshToken();
        LocalDateTime expiresAt = jwtUtil.getRefreshTokenExpiresAt();

        // DB에 Refresh Token 저장 (이미 있으면 갱신)
        refreshTokenRepository.findByUser(user)
                .ifPresentOrElse(
                        token -> token.updateToken(refreshToken, expiresAt),
                        () -> refreshTokenRepository.save(RefreshToken.create(user, refreshToken, expiresAt))
                );

        return new LoginResponseDto(accessToken, refreshToken);
    }

    // Access Token 재발급
    public TokenResponseDto reissueAccessToken(RefreshRequestDto dto) {
        // 1. DB에서 Refresh Token 확인
        RefreshToken refreshToken = refreshTokenRepository.findByToken(dto.refreshToken())
                .orElseThrow(RefreshTokenNotFoundException::new);

        // 2. 만료 여부 확인
        if (refreshToken.isExpired()) {
            throw new RefreshTokenExpiredException();
        }

        // 3. 새 Access Token 발급
        String newAccessToken = jwtUtil.generateAccessToken(refreshToken.getUser().getId());

        return new TokenResponseDto(newAccessToken);
    }

    // 로그아웃: 해당 유저의 Refresh Token을 DB에서 삭제
    @Transactional
    public void logout(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(userId));

        // 이미 없어도(중복 로그아웃 등) 에러 낼 필요 없이 조용히 넘어감
        refreshTokenRepository.deleteByUser(user);
    }
}
