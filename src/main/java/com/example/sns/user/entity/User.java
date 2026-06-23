package com.example.sns.user.entity;

import com.example.sns.security.util.PasswordEncoder;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;


@Entity
@Getter
@Table(name = "users")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String password;

    private User(String email, String name, String password) {
        this.email = email;
        this.name = name;
        this.password = password;
    }


    public static User create(String email, String name, String password) {
        String encodedPassword = PasswordEncoder.encode(password);
        return new User(email, name, encodedPassword);
    }
}
