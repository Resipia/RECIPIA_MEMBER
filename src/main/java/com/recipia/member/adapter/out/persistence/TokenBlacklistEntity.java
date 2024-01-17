package com.recipia.member.adapter.out.persistence;


import com.recipia.member.adapter.out.persistence.auditingfield.CreateDateTimeForEntity;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.Objects;

/** 토큰 블랙리스트 (access token) */
@ToString(callSuper = true)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
@Table(name = "token_blacklist")
public class TokenBlacklistEntity extends CreateDateTimeForEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "blacklist_id")
    private Long id;

    @Column(name = "token", nullable = false)
    private String token;

    @Column(name = "expired_dttm", nullable = false)
    private LocalDateTime expiredDateTime;

    @Builder
    private TokenBlacklistEntity(Long id, String token, LocalDateTime expiredDateTime) {
        this.id = id;
        this.token = token;
        this.expiredDateTime = expiredDateTime;
    }

    public static TokenBlacklistEntity of(Long id, String token, LocalDateTime expiredDateTime) {
        return new TokenBlacklistEntity(id, token, expiredDateTime);
    }

    public static TokenBlacklistEntity of(String token, LocalDateTime expiredDateTime) {
        return new TokenBlacklistEntity(null, token, expiredDateTime);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof TokenBlacklistEntity that)) return false;
        return this.id != null && Objects.equals(getId(), that.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }
}
