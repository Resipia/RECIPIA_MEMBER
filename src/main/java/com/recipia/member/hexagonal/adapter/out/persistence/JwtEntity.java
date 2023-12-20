package com.recipia.member.hexagonal.adapter.out.persistence;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.Objects;

/** JWT  */
@ToString(callSuper = true)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
@Table(name = "jwt")
public class JwtEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "jwt_id", nullable = false)
    private Long id;                // jwt pk

    @Column(name = "member_id", nullable = false)
    private Long memberId;          // 회원 pk

    @Column(name = "refresh_token", nullable = false, length = 500)
    private String refreshToken;

    @Column(name = "expired_dttm", nullable = false)
    private LocalDateTime expiredDateTime;


    @Builder
    private JwtEntity(Long id, Long memberId, String refreshToken, LocalDateTime expiredDateTime) {
        this.id = id;
        this.memberId = memberId;
        this.refreshToken = refreshToken;
        this.expiredDateTime = expiredDateTime;
    }

    // 새 엔티티 생성용 팩토리 메소드
    public static JwtEntity of(Long memberId, String refreshToken, LocalDateTime expiredDateTime) {
        return new JwtEntity(null, memberId, refreshToken, expiredDateTime);
    }

    // 기존 엔티티 로드용 팩토리 메소드
    public static JwtEntity of(Long id, Long memberId, String refreshToken, LocalDateTime expiredDateTime) {
        return new JwtEntity(id, memberId, refreshToken, expiredDateTime);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof JwtEntity jwt)) return false;
        return this.id != null && Objects.equals(getId(), jwt.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }
}
