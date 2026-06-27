package com.example.sns.controller;


import java.time.Duration;

import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.sns.dto.LoginRequestDto;
import com.example.sns.dto.LoginResponseDto;
import com.example.sns.exception.LoginFailedException;
import com.example.sns.service.AuthService;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class AuthPageController {
    private static final String ACCESS_TOKEN_COOKIE_NAME = "ACCESS_TOKEN";

    private final AuthService authService;

    @GetMapping("/login")
    public String loginPage(){
        return "login";
    }

    @PostMapping("/login")
    public String login(
        @RequestParam("username") String username,
        @RequestParam("password") String password,
        HttpServletResponse response,
        Model model
    ){
        try{
            LoginRequestDto requestDto = new LoginRequestDto(username, password);
            LoginResponseDto responseDto = authService.login(requestDto);
            ResponseCookie cookie = ResponseCookie.from(ACCESS_TOKEN_COOKIE_NAME, responseDto.accessToken())
                .httpOnly(true)
                .path("/")
                .maxAge(Duration.ofHours(1))
                .sameSite("Lax")
                .build();

            response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());

            return "redirect:/home";
        }catch(LoginFailedException e){
            model.addAttribute("error", e.getMessage());
            return "login";
        }
    }
    @PostMapping("/logout")
    public String logout(HttpServletResponse response){
        ResponseCookie cookie = ResponseCookie.from(ACCESS_TOKEN_COOKIE_NAME, "")
            .httpOnly(true)
            .path("/")
            .maxAge(0)
            .sameSite("Lax")
            .build();

        response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());

        return "redirect:/login";
    }
}
