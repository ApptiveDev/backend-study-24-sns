package com.example.sns.exception;

import com.example.sns.entity.User;

public class UserNotFoundException extends RuntimeException{
    public UserNotFoundException() {
        super("사용자가 존재하지 않습니다.");
    }
}
