package com.example.sns.controller;

import com.example.sns.dto.UserCreateRequest;
import com.example.sns.dto.UserResponse;
import com.example.sns.service.UserService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    // 회원가입
    @PostMapping
    public UserResponse createUser(@RequestBody UserCreateRequest request) {
        return userService.createUser(request);
    }
}