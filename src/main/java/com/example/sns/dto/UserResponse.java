package com.example.sns.dto;

import com.example.sns.entity.User;

import java.time.LocalDateTime;

public record UserResponse(
        Long userId,
        String email,
        String nickname,
        LocalDateTime createdAt
) {
    public static UserResponse from(User user) {
        return new UserResponse(
                user.getId(),
                user.getEmail(),
                user.getNickname(),
                user.getCreatedAt()
        );
    }
}