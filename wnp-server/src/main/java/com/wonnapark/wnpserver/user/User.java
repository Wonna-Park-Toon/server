package com.wonnapark.wnpserver.user;

import com.wonnapark.wnpserver.global.common.BaseEntity;
import com.wonnapark.wnpserver.oauth.OAuthProvider;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Where;

import java.time.LocalDateTime;

@Table(name = "users")
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Where(clause = "is_deleted IS NULL")
public class User extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "provider_id", nullable = false)
    private String providerId;

    @Enumerated(EnumType.STRING)
    @Column(name = "platform", nullable = false)
    private OAuthProvider platform;

    @Column(name = "nickname", unique = true, nullable = false)
    private String nickname;

    @Column(name = "age")
    private int age;

    @Column(name = "gender", nullable = false)
    private String gender;

    @Column(name = "role", nullable = false)
    @Enumerated(EnumType.STRING)
    private Role role;

    @Column(name = "is_deleted", nullable = true, columnDefinition = "TIMESTAMP(6)")
    private LocalDateTime isDeleted;

    @Builder
    private User(String providerId, OAuthProvider platform, String nickname, int age, String gender, Role role) {
        this.providerId = providerId;
        this.platform = platform;
        this.nickname = nickname;
        this.age = age;
        this.gender = gender;
        this.role = role;
    }
}
