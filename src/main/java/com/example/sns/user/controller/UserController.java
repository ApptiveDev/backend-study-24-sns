package com.example.sns.user.controller;

import com.example.sns.user.entity.User;
import com.example.sns.user.dto.SignupRequest;
import com.example.sns.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    // 회원가입
    @PostMapping
    public ResponseEntity<User> create(@RequestBody SignupRequest request) {
        User user = userService.createUser(request);
        return ResponseEntity.ok(user);
    }

    // 모든 사용자 조회
    @GetMapping
    public ResponseEntity<List<User>> getAll() {
        userService.findAllUsers();
        return ResponseEntity.ok(userService.findAllUsers());
    }
}
