package com.example.sns.repository;

import com.example.sns.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;

//JPA의 경우, JpaRepository를 상속받아 해당 인터페이스에 함수 선언을 하는 것으로 DB가 전부 구성됨

public interface JPARepository extends JpaRepository<Users, Long> {
    Users findByUsername(String username); // JPA는 이렇게 findBy__ 방식으로 함수를 만들경우 sql이 자동 생성
}
