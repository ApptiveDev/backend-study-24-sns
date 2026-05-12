package com.example.sns.repository;

import com.example.sns.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<Users, Long> {

    Users findByUsername(String username);

    //유저가 있는지 없는지 판별
    boolean existsByUsername(String username);
}