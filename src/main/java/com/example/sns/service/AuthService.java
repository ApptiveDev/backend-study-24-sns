package com.example.sns.service;

import com.example.sns.auth.JwtUtil;
import com.example.sns.dto.LoginRequest;
import com.example.sns.dto.LoginResponse;
import com.example.sns.entity.User;
import com.example.sns.exception.CustomException;
import com.example.sns.exception.ErrorCode;
import com.example.sns.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class AuthService {

    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;

    public AuthService(UserRepository userRepository, JwtUtil jwtUtil) {
        this.userRepository = userRepository;
        this.jwtUtil = jwtUtil;
    }

    // 로그인
    public LoginResponse login(LoginRequest request) {
        User user = userRepository.findByEmail(request.email())
                .orElseThrow(() -> new CustomException(ErrorCode.EMAIL_NOT_FOUND));

        if (!user.getPassword().equals(request.password())) {
            throw new CustomException(ErrorCode.INVALID_PASSWORD);
        }

        String accessToken = jwtUtil.createAccessToken(
                user.getId(),
                user.getEmail()
        );

        return new LoginResponse(accessToken);
    }
}