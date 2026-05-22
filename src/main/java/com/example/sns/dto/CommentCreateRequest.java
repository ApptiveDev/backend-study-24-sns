package com.example.sns.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class CommentCreateRequest {

    @NotNull(message = "postId is required.")
    private Long postId;

    @NotBlank(message = "content is required.")
    @Size(max = 1000, message = "content must be 1000 characters or less.")
    private String content;
}
