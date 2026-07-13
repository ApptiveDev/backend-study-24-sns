package com.example.sns.service;

import com.example.sns.auth.JwtUtil;
import com.example.sns.dto.*;
import com.example.sns.entity.RefreshToken;
import com.example.sns.entity.User;
import com.example.sns.exception.DuplicateEmailException;
import com.example.sns.exception.InvalidLoginException;
import com.example.sns.exception.RefreshTokenExpiredException;
import com.example.sns.exception.RefreshTokenNotFoundException;
import com.example.sns.repository.RefreshTokenRepository;
import com.example.sns.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true) // 기본적으로 읽기 전용으로 설정 (성능 최적화)
public class UserService {

    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;
    private final RefreshTokenRepository refreshTokenRepository;
    private final PasswordEncoder passwordEncoder;

    // 유저 등록 (회원가입)
    @Transactional // 저장할 때는 쓰기 권한이 필요함
    public UserResponseDto createUser(UserRequestDto dto) {
        // 중복 이메일 체크
        if (userRepository.existsByEmail(dto.email())) {
            throw new DuplicateEmailException(dto.email());
        }

        // 비밀번호 해싱 후 저장
        String encodedPassword = passwordEncoder.encode(dto.password());
        User user = User.create(dto.username(), dto.email(), encodedPassword);
        User savedUser = userRepository.save(user);

        // 엔티티를 응답 DTO로 변환해서 반환
        return new UserResponseDto(savedUser.getId(), savedUser.getUsername());
    }

    // 마이페이지용 유저 단건 조회 (없으면 null)
    public User getUserOrNull(Long userId) {
        return userRepository.findById(userId).orElse(null);
    }

    // 유저 전체 조회 (테스트용)
    public List<UserResponseDto> getAllUsers() {
        return userRepository.findAll().stream()
                .map(user -> new UserResponseDto(user.getId(), user.getUsername()))
                .toList();
    }
}
