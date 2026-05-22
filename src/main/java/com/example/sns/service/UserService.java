package com.example.sns.service;

import com.example.sns.dto.UserCreateRequest;
import com.example.sns.dto.UserResponse;
import com.example.sns.entity.User;
import com.example.sns.exception.CustomException;
import com.example.sns.exception.ErrorCode;
import com.example.sns.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    // 회원가입
    @Transactional
    public UserResponse createUser(UserCreateRequest request) {
        if (userRepository.findByEmail(request.email()).isPresent()) {
            throw new CustomException(ErrorCode.EMAIL_DUPLICATED);
        }

        User user = User.create(
                request.email(),
                request.nickname(),
                request.password()
        );

        User savedUser = userRepository.save(user);

        return UserResponse.from(savedUser);
    }
}