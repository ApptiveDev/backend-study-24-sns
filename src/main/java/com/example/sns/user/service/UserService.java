package com.example.sns.user.service;

import com.example.sns.user.dto.UserResponse;
import com.example.sns.user.entity.User;
import com.example.sns.user.repository.UserRepository;
import com.example.sns.user.dto.SignupRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserService {

    private final UserRepository userRepository;

    @Transactional
    public UserResponse createUser(SignupRequest request) {

        if (userRepository.existsByEmail(request.email())) {
            throw new IllegalArgumentException("이미 존재하는 이메일입니다.");
        }

        User user = new User(request.email(), request.password());
        userRepository.save(user);

        return UserResponse.from(user);
    }

    // 개별 조회
    public UserResponse findById(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(
                () -> new IllegalArgumentException("사용자 정보가 없습니다.")
        );

        return UserResponse.from(user);
    }

    // 전체 사용자 조회
    public List<UserResponse> findAllUsers() {
        return userRepository.findAll()
                .stream()
                .map(UserResponse::from)
                .toList();
    }

}
