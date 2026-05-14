package com.example.sns.post.dto;

public record PostUpdateRequest (Long userId, String title, String content) {
}
