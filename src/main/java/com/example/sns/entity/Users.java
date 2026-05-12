package com.example.sns.entity;



import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalDateTime;

@Entity //이게 entity라는 어노테이션
@Setter //@Getter, @Setter와 같이 이런 lombok 어노테이션 붙여주면 getId(), setId(), getUsername() 메서드 전부 생성해줌
@Getter
@NoArgsConstructor // 파라미터가 없는 기본 생성자를 자동으로 만들어줌 (JPA에서는 이거 필수로 사용한다)
public class Users {
    //참고) Lombok 안쓴다면 자신 참조 포인터는 this로 한다

    @Id // 기본키(PK) 지정
    @GeneratedValue(strategy = GenerationType.IDENTITY) // AUTO_INCREMENT (1, 2, 3... 자동 증가)
    private Long id;

    @Column(nullable = false, unique = true) // null 허용 안함, 중복 불가
    private String username;

    @Column(nullable = false)
    private String password;

    private String email;
    private String role; //관리자인지 유저인지 구분은 해야지

    private LocalDateTime createdAt;

    @PrePersist //DB에 처음 저장되기 전 자동으로 현재 시간 저장하는 어노테이션
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }
}
