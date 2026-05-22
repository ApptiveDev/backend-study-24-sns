package com.example.sns.controller;

import com.example.sns.auth.AuthInterceptor;
import com.example.sns.dto.UserCreateRequest;
import com.example.sns.dto.UserResponse;
import com.example.sns.dto.UserUpdateRequest;
import com.example.sns.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    @PostMapping
    public ResponseEntity<UserResponse> create(@Valid @RequestBody UserCreateRequest request) {
        UserResponse response = userService.create(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    public ResponseEntity<List<UserResponse>> findAll() {
        return ResponseEntity.ok(userService.findAll());
    }

    @GetMapping("/{userId}")
    public ResponseEntity<UserResponse> findById(@PathVariable Long userId) {
        return ResponseEntity.ok(userService.findById(userId));
    }

    @PatchMapping("/{userId}")
    public ResponseEntity<UserResponse> update(
            @PathVariable Long userId,
            @RequestAttribute(AuthInterceptor.AUTH_USER_ID) Long authUserId,
            @Valid @RequestBody UserUpdateRequest request
    ) {
        return ResponseEntity.ok(userService.update(userId, authUserId, request));
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<Void> delete(
            @PathVariable Long userId,
            @RequestAttribute(AuthInterceptor.AUTH_USER_ID) Long authUserId
    ) {
        userService.delete(userId, authUserId);
        return ResponseEntity.noContent().build();
    }
}
