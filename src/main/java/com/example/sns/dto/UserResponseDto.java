package com.example.sns.dto;

import com.example.sns.entity.User;

public record UserResponseDto(
    Long id,
    String username,
    String email
) {
    public static UserResponseDto from(User user){
        return new UserResponseDto(
            user.getId(),
            user.getUsername(),
            user.getEmail()
        );
    }
}
