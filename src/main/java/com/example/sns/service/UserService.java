package com.example.sns.service;

import com.example.sns.auth.JwtUtil;
import com.example.sns.dto.LoginRequestDto;
import com.example.sns.dto.LoginResponseDto;
import com.example.sns.dto.UserRequestDto;
import com.example.sns.dto.UserResponseDto;
import com.example.sns.entity.User;
import com.example.sns.exception.DuplicateEmailException;
import com.example.sns.exception.InvalidLoginException;
import com.example.sns.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true) // 기본적으로 읽기 전용으로 설정 (성능 최적화)
public class UserService {

    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;

    // 유저 등록 (회원가입)
    @Transactional // 저장할 때는 쓰기 권한이 필요함
    public UserResponseDto createUser(UserRequestDto dto) {
        // 중복 이메일 체크
        if (userRepository.existsByEmail(dto.email())) {
            throw new DuplicateEmailException(dto.email());
        }

        User user = new User(dto.username(), dto.email(), dto.password());
        User savedUser = userRepository.save(user);

        // 엔티티를 응답 DTO로 변환해서 반환
        return new UserResponseDto(savedUser.getId(), savedUser.getUsername());
    }

    // 로그인
    public LoginResponseDto login(LoginRequestDto dto) {
        // 1. 이메일로 유저 찾기
        User user = userRepository.findByEmail(dto.email())
                .orElseThrow(InvalidLoginException::new);

        // 2. 비밀번호 확인
        if (!user.getPassword().equals(dto.password())) {
            throw new InvalidLoginException();
        }

        // 3. JWT 발급
        String token = jwtUtil.generateToken(user.getId());
        return new LoginResponseDto(token);
    }

    // 유저 전체 조회 (테스트용)
    public List<UserResponseDto> getAllUsers() {
        return userRepository.findAll().stream()
                .map(user -> new UserResponseDto(user.getId(), user.getUsername()))
                .toList();
    }
}
