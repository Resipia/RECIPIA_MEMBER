package com.recipia.member.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Builder
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

    /**
     * 테스트에서 사용하기 위해 id만 사용하는 팩토리 메소드
     */
    public static Jwt of(Long id) {
        return new Jwt(id, null, null, null);
    }

    /**
     * Refresh token의 유효성을 확인한다.
     *
     * @param now 현재 시간
     * @param expiredDateTime 토큰의 만료 시간
     * @return 현재 시간이 expiredDateTime보다 이전이면 true, 그렇지 않으면 false
     */
    public static boolean isRefreshTokenValid(LocalDateTime now, LocalDateTime expiredDateTime) {
        return now.isBefore(expiredDateTime);
    }


}
