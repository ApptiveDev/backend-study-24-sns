package com.example.sns.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CommentCreateRequestDto(
        @NotBlank(message = "내용은 필수입니다.")
        String content,
        @NotNull
        Long userId
) {}
