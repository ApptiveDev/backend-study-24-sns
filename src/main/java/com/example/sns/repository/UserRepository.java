package com.example.sns.repository;

import com.example.sns.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    // 이메일로 유저를 조회한다.
    Optional<User> findByEmail(String email);
}