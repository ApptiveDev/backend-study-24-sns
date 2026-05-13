package com.example.sns.controller;

import com.example.sns.dto.UserRequestDto;
import com.example.sns.dto.UserResponseDto;
import com.example.sns.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor // 생성자 주입을 자동으로 해줍니다 (final 필드 대상)
public class UserController {
    private final UserService userService;

    // 1. 유저 생성 (회원 가입)
    @PostMapping
    public ResponseEntity<UserResponseDto> createUser(@RequestBody @Valid UserRequestDto dto) {
        // @Valid로 검증하고, 201 Created 상태 코드로 응답
        UserResponseDto createdUser = userService.createUser(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdUser);
    }

    // 2. 유저 전체 조회
    @GetMapping
    public ResponseEntity<List<UserResponseDto>> getAllUsers() {
        List<UserResponseDto> users = userService.getAllUsers();
        return ResponseEntity.ok(users);
    }
}
