package com.example.sns.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.sns.auth.JwtTokenProvider;
import com.example.sns.dto.LoginRequestDto;
import com.example.sns.dto.LoginResponseDto;
import com.example.sns.entity.User;
import com.example.sns.exception.LoginFailedException;
import com.example.sns.repository.UserRepository;

@Service
@Transactional(readOnly = true)
public class AuthService {
    private final UserRepository userRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public AuthService(
        UserRepository userRepository,
        JwtTokenProvider jwtTokenProvider,
        PasswordEncoder passwordEncoder
    ){
        this.userRepository = userRepository;
        this.jwtTokenProvider = jwtTokenProvider;
        this.passwordEncoder = passwordEncoder;
    }

    public LoginResponseDto login(LoginRequestDto requestDto){
        User user = userRepository.findByUsername(requestDto.username())
            .orElseThrow(() -> new LoginFailedException("아이디 또는 비밀번호가 올바르지 않습니다."));

        if(!passwordEncoder.matches(requestDto.password(), user.getPassword())) {
            throw new LoginFailedException("아이디 또는 비밀번호가 올바르지 않습니다.");
        }

        String accessToken = jwtTokenProvider.createAccessToken(user.getId(), user.getUsername());

        return new LoginResponseDto(accessToken);
    }
}
