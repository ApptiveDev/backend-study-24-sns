package com.example.sns.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String username;
    private String email;
    private String password;

    protected User(){
    }

    public static User createUser(String username, String email, String encodedPassword){
        validateUsername(username);
        validateEmail(email);
        validatePassword(encodedPassword);

        User user = new User();
        user.username = username;
        user.email = email;
        user.password = encodedPassword;
        return user;
    }

    private static void validateUsername(String username){
        if(username == null || username.isBlank()){
            throw new IllegalArgumentException("사용자 이름은 필수입니다.");
        }
    }

    private static void validateEmail(String email){
        if(email == null || email.isBlank()){
            throw new IllegalArgumentException("이메일은 필수입니다.");
        }
    }

    private static void validatePassword(String password){
        if(password == null || password.isBlank()){
            throw new IllegalArgumentException("비밀번호는 필수입니다.");
        }
    }

    public Long getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword(){
        return password;
    }
}
