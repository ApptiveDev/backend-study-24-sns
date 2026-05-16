package com.example.sns.controller;

import com.example.sns.entity.User;
import com.example.sns.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    // 유저 생성 (POST /users)
    @PostMapping
    public ResponseEntity<User> createUser(@RequestBody UserCreateRequest request) {
        User user = userService.createUser(request.name(), request.email());

        return ResponseEntity.status(HttpStatus.CREATED).body(user);
    }

    public record UserCreateRequest(
            String name,
            String email
    ) {}
}