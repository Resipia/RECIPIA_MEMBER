package com.recipia.member.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class Jwt {

    private Long id;
    private Long memberId;
    private String refreshToken;
    private LocalDateTime expiredDateTime;

    private Jwt(Long id, Long memberId, String refreshToken, LocalDateTime expiredDateTime) {
        this.id = id;
        this.memberId = memberId;
        this.refreshToken = refreshToken;
        this.expiredDateTime = expiredDateTime;
    }

    public static Jwt of(Long id, Long memberId, String refreshToken, LocalDateTime expiredDateTime) {
        return new Jwt(id, memberId, refreshToken, expiredDateTime);
    }

    public static Jwt of(Long memberId, String refreshToken, LocalDateTime expiredDateTime) {
        return new Jwt(null, memberId, refreshToken, expiredDateTime);
    }
}
