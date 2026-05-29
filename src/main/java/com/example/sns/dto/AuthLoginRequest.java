package com.example.sns.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class AuthLoginRequest {

    @NotBlank(message = "username is required.")
    private String username;

    public AuthLoginRequest(String username) {
        this.username = username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
