package com.example.sns.service;

import com.example.sns.dto.UserCreateRequest;
import com.example.sns.dto.UserResponse;
import com.example.sns.dto.UserUpdateRequest;
import com.example.sns.entity.User;
import com.example.sns.exception.BadRequestException;
import com.example.sns.exception.ErrorCode;
import com.example.sns.exception.NotFoundException;
import com.example.sns.repository.UserRepository;
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
    public UserResponse create(UserCreateRequest request) {
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new BadRequestException(ErrorCode.DUPLICATE_USERNAME);
        }

        User user = User.create(request.getUsername(), request.getNickname()); 
        User savedUser = userRepository.save(user);

        return UserResponse.from(savedUser);
    }

    public List<UserResponse> findAll() {
        return userRepository.findAll()
                .stream()
                .map(UserResponse::from)
                .toList();
    }

    public UserResponse findById(Long userId) {
        User user = getUser(userId);
        return UserResponse.from(user);
    }

    @Transactional
    public UserResponse update(Long userId, UserUpdateRequest request) {
        User user = getUser(userId);
        user.update(request.getNickname());

        return UserResponse.from(user);
    }

    @Transactional
    public void delete(Long userId) {
        User user = getUser(userId);
        userRepository.delete(user);
    }

    private User getUser(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException(ErrorCode.USER_NOT_FOUND));
    }
}
