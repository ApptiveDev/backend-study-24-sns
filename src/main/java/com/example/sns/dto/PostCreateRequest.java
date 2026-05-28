package com.example.sns.dto;

import jakarta.validation.constraints.NotBlank;

// 게시글 생성 시 사용하는 DTO다.
public record PostCreateRequest(
        @NotBlank(message = "제목을 입력해주세요.")
        String title,

        @NotBlank(message = "내용을 입력해주세요.")
        String content
) {}