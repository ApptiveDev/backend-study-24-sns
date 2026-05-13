package com.example.sns.service;

import com.example.sns.dto.UserRequestDto;
import com.example.sns.dto.UserResponseDto;
import com.example.sns.entity.User;
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

    // 유저 등록 (회원가입)
    @Transactional // 저장할 때는 쓰기 권한이 필요함
    public UserResponseDto createUser(UserRequestDto dto) {
        // 중복 이메일 체크
        if (userRepository.existsByEmail(dto.email())) {
            throw new IllegalArgumentException("이미 존재하는 이메일입니다: " + dto.email());
        }

        User user = new User(dto.username(), dto.email(), dto.password());
        User savedUser = userRepository.save(user);

        // 엔티티를 응답 DTO로 변환해서 반환
        return new UserResponseDto(savedUser.getId(), savedUser.getUsername());
    }

    // 유저 전체 조회 (테스트용)
    public List<UserResponseDto> getAllUsers() {
        return userRepository.findAll().stream()
                .map(user -> new UserResponseDto(user.getId(), user.getUsername()))
                .toList();
    }
}
