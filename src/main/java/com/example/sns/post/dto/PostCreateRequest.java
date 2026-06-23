package com.example.sns.post.dto;

public record PostCreateRequest (Long userId, String title, String content) {
}
