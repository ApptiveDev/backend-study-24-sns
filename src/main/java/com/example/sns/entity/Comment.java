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
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String content;

    //다대일은 꼭 FetchType.LAZY 해줘라
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id") //FK
    private Post post;

    //다대일은 꼭 FetchType.LAZY 해줘라
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id") //FK
    private Users author;

    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }
}