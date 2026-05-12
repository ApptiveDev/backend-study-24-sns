package com.example.sns.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(columnDefinition = "TEXT", nullable = false) // 아주 긴 텍스트를 담기 위해 설정 (null도 막아라)
    //이게 뭔소리냐면 string을 그냥 선언할 경우 VARCHAR(255)으로 설정해버리기 때문에 columnDefinition = "TEXT"이란 설정으로 text 전용 칸을 만들라는 것
    private String content;

    private int viewCount = 0; // 기본값 0


    @ManyToOne(fetch = FetchType.LAZY)
    //글(N) 입장에서 작성자(1)와의 다대일관계를 맺음
    //DB와의 I/O(통신)에서 병목 및 성능을 위해서는 지연연산이 필수임
    // 게시글(Post) 목록을 띄울 때, 게시글 정보와 함께 작성자의 '이름' 정도만 필요한데, 굳이 Users 전체 정보까지 DB에서 전부 끌고 올 필요가 없을 때를 생각해보면
    //기본값인 fetch = FetchType.EAGER인 즉시로딩이면?
    //==> Post + Users 정보까지 전부 조회
    //fetch = FetchType.LAZY(지연 로딩)이면?
    //==> Post + Users 대신 대신 가짜(프록시) 객체
    @JoinColumn(name = "user_id") //FK
    // 실제 DB 테이블에 생길 외래키(FK) 컬럼명
    //author 대신 user_id라는 컬럼명을 쓰라고 강제하는 어노테이션이라고 생각하면 된다(FK를 생성하는 거라서 필요함. 이거 안쓰면 그냥 스프링JPA가 author_id라고 임의로 이름 지어버림)
    //보통 무조건 유저 번호는 user_id로 통일 이런 방식으로 명세서에서 합의하니까 이건 꼭 써서 제대로 생성되도록 해줘야 함
    private Users author;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    // 데이터가 수정될 때마다 자동으로 시간을 갱신
    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}