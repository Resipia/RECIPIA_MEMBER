package com.recipia.member.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 로그아웃 도메인 객체
 */
@Getter
@NoArgsConstructor
public class Logout {

    private Long memberId;
    private String accessToken;

    @Builder
    private Logout(Long memberId, String accessToken) {
        this.memberId = memberId;
        this.accessToken = accessToken;
    }

    public static Logout of(Long memberId, String accessToken) {
        return new Logout(memberId, accessToken);
    }
}
