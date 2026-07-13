package com.example.sns;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

// Annotation(어노테이션)(@)은 "바로 다음에 나오는 대상"에 대한 설정이나 설명을 담당
// 클래스 바로 위: 해당 클래스 전체의 성격이나 설정을 정의 (예: @Entity, @Table)
// 변수(필드) 바로 위: 해당 변수 하나에만 적용되는 규칙을 정의 (예: @Id, @Column)
// 메서드 바로 위: 해당 메서드가 실행될 때의 규칙을 정의 (예: @PrePersist)

// 스프링부트 자동 실행 어노테이션 (아래 세 가지의 기능을 모두 포함)
// @Configuration : 스프링의 설정 파일임을 명시
// @ComponentScan : 하위 패키지에서 빈을 검색
// @EnableAutoConfiguration : 개발자가 작성해둔 코드 및 라이브러리를 스캔해서 자동으로 설정
@SpringBootApplication
@EnableJpaAuditing
public class SnsApplication {

    public static void main(String[] args) {
        SpringApplication.run(SnsApplication.class, args);
    }

}
