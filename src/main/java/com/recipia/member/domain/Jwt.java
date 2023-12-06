package com.recipia.member.domain;

import com.recipia.member.hexagonal.adapter.out.persistence.member.Member;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.Objects;

/** JWT  */
@ToString(callSuper = true)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
public class Jwt {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "jwt_id", nullable = false)
    private Long id;                // jwt pk

    @ToString.Exclude
    @JoinColumn(name = "member_id", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private Member member;          // 회원 pk

    @Column(name = "refresh_token", nullable = false, length = 500)
    private String refreshToken;

    @Column(name = "expired_dttm", nullable = false)
    private LocalDateTime expiredDateTime;


    @Builder
    private Jwt(Long id, Member member, String refreshToken, LocalDateTime expiredDateTime) {
        this.id = id;
        this.member = member;
        this.refreshToken = refreshToken;
        this.expiredDateTime = expiredDateTime;
    }

    public static Jwt of(Member member, String refreshToken, LocalDateTime expiredDateTime) {
        return new Jwt(null, member, refreshToken, expiredDateTime);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Jwt jwt)) return false;
        return this.id != null && Objects.equals(getId(), jwt.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }
}
