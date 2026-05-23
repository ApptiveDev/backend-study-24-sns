package com.example.sns.service;

import com.example.sns.dto.LoginRequest;
import com.example.sns.dto.LoginResponse;
import com.example.sns.dto.SignupRequest;
import com.example.sns.entity.User;
import com.example.sns.exception.DuplicateEmailException;
import com.example.sns.exception.InvalidPasswordException;
import com.example.sns.exception.UserNotFoundException;
import com.example.sns.jwt.JwtTokenProvider;
import com.example.sns.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserRepository userRepository;
    private final JwtTokenProvider jwtTokenProvider;

    @Transactional
    public void signup(SignupRequest request) {
        if (userRepository.existsByEmail(request.email())) {
            throw new DuplicateEmailException();
        }

        User user = User.create(
                request.username(),
                request.email(),
                request.password()
        );

        userRepository.save(user);
    }

    @Transactional(readOnly = true)
    public LoginResponse login(LoginRequest request) {
        User user = userRepository.findByEmail(request.email())
                .orElseThrow(UserNotFoundException::new);

        if (!user.getPassword().equals(request.password())) {
            throw new InvalidPasswordException();
        }

        String accessToken = jwtTokenProvider.createAccessToken(user.getId());

        return new LoginResponse(accessToken);
    }
}
