package com.example.sns.user.service;

import com.example.sns.exception.BusinessException;
import com.example.sns.exception.DuplicateEmailException;
import com.example.sns.exception.ErrorCode;
import com.example.sns.exception.UserNotFoundException;
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
            throw new BusinessException(ErrorCode.DUPLICATE_EMAIL);
        }

        User user = User.create(request.email(), request.name(), request.password());

        userRepository.save(user);

        return UserResponse.from(user);
    }

    // 개별 조회
    public UserResponse findById(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(
                () -> new BusinessException(ErrorCode.USER_NOT_FOUND)
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
