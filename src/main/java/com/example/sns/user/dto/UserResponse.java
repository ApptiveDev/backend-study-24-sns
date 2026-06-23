package com.example.sns.user.dto;

import com.example.sns.user.entity.User;

public record UserResponse(
        Long id,
        String userName
) {

    public static UserResponse from(User user) {
        return new UserResponse(
                user.getId(),
                user.getName()
        );
    }
}

