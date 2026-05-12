package com.example.sns.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

@Entity //이게 entity라는 어노테이션
@Setter //@Getter, @Setter와 같이 이런 lombok 어노테이션 붙여주면 getId(), setId(), getUsername() 메서드 전부 생성해줌
@Getter
public class Users {
    //자신 참조는 this로 한다

    @Id //@ID를 이용해 해당 필드를 기본 키로 설정도 가능하다
    @GeneratedValue(strategy = GenerationType.IDENTITY) //@GeneratedValue를 이용하여서 자동 증가 ID 필드를 만들수 있다
    private int id;
    private String username;
    private String password;
}
