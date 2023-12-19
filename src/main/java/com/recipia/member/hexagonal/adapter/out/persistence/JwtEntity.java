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
public class JwtEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "jwt_id", nullable = false)
    private Long id;                // jwt pk

    @ToString.Exclude
    @JoinColumn(name = "member_id", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private MemberEntity member;          // 회원 pk

    @Column(name = "refresh_token", nullable = false, length = 500)
    private String refreshToken;

    @Column(name = "expired_dttm", nullable = false)
    private LocalDateTime expiredDateTime;


    @Builder
    private JwtEntity(Long id, MemberEntity member, String refreshToken, LocalDateTime expiredDateTime) {
        this.id = id;
        this.member = member;
        this.refreshToken = refreshToken;
        this.expiredDateTime = expiredDateTime;
    }

    // 새 엔티티 생성용 팩토리 메소드
    public static JwtEntity of(MemberEntity member, String refreshToken, LocalDateTime expiredDateTime) {
        return new JwtEntity(null, member, refreshToken, expiredDateTime);
    }

    // 기존 엔티티 로드용 팩토리 메소드
    public static JwtEntity of(Long id, MemberEntity member, String refreshToken, LocalDateTime expiredDateTime) {
        return new JwtEntity(id, member, refreshToken, expiredDateTime);
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
