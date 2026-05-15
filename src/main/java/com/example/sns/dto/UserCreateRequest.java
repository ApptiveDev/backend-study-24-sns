package com.example.sns.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class UserCreateRequest {

    @NotBlank(message = "username is required.")
    @Size(max = 30, message = "username must be 30 characters or less.")
    private String username;

    @NotBlank(message = "nickname is required.")
    @Size(max = 30, message = "nickname must be 30 characters or less.")
    private String nickname;
}
