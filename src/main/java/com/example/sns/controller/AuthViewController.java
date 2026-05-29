package com.example.sns.controller;

import com.example.sns.dto.AuthLoginRequest;
import com.example.sns.dto.TokenResponse;
import com.example.sns.entity.User;
import com.example.sns.exception.BusinessException;
import com.example.sns.repository.UserRepository;
import com.example.sns.service.AuthService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@RequiredArgsConstructor
public class AuthViewController {

    private static final String LOGIN_USER_ID = "loginUserId";
    private static final String ACCESS_TOKEN = "accessToken";
    private static final String REFRESH_TOKEN = "refreshToken";

    private final AuthService authService;
    private final UserRepository userRepository;

    @GetMapping("/")
    public String index() {
        return "redirect:/login";
    }

    @GetMapping("/login")
    public String loginForm(Model model) {
        model.addAttribute("loginRequest", new AuthLoginRequest());
        return "login";
    }

    @PostMapping("/login")
    public String login(
            @Valid @ModelAttribute("loginRequest") AuthLoginRequest request,
            BindingResult bindingResult,
            HttpSession session
    ) {
        if (bindingResult.hasErrors()) {
            return "login";
        }

        TokenResponse tokenResponse;
        User user;
        try {
            tokenResponse = authService.login(request);
            user = userRepository.findByUsername(request.getUsername()).orElseThrow();
        } catch (BusinessException exception) {
            bindingResult.reject("loginFail", exception.getMessage());
            return "login";
        }

        session.setAttribute(LOGIN_USER_ID, user.getId());
        session.setAttribute(ACCESS_TOKEN, tokenResponse.accessToken());
        session.setAttribute(REFRESH_TOKEN, tokenResponse.refreshToken());

        return "redirect:/home";
    }

    @GetMapping("/home")
    public String home(HttpSession session, Model model) {
        Long userId = (Long) session.getAttribute(LOGIN_USER_ID);
        if (userId == null) {
            return "redirect:/login";
        }

        User user = userRepository.findById(userId).orElseThrow();
        model.addAttribute("user", user);
        model.addAttribute("accessToken", session.getAttribute(ACCESS_TOKEN));

        return "home";
    }

    @PostMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/login";
    }
}
