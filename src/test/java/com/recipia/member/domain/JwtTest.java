package com.recipia.member.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("[단위] Jwt 도메인 테스트")
class JwtTest {

    @DisplayName("[happy] refresh token의 만료 날짜가 오늘보다 이후일때 true를 반환한다.")
    @Test
    void isRefreshTokenValidTrue() {
        //given
        Jwt jwt = createJwtUsingValidRefresh();
        //when
        boolean isRefreshTokenValid = Jwt.isRefreshTokenValid(LocalDateTime.now(), jwt.getExpiredDateTime());
        //then
        assertThat(isRefreshTokenValid).isTrue();
    }

    @DisplayName("[bad] refresh token의 만료 날짜가 오늘보다 이전일때 false를 반환한다.")
    @Test
    void isRefreshTokenValidFalse() {
        //given
        Jwt jwt = createJwtUsingInvalidRefresh();
        //when
        boolean isRefreshTokenValid = Jwt.isRefreshTokenValid(LocalDateTime.now(), jwt.getExpiredDateTime());
        //then
        assertThat(isRefreshTokenValid).isFalse();
    }

    public Jwt createJwtUsingValidRefresh () {
        LocalDateTime sixMonthsLater = LocalDateTime.now().plusMonths(6);
        return Jwt.of(1L, 1L, "refreshToken", sixMonthsLater);
    }

    public Jwt createJwtUsingInvalidRefresh () {
        LocalDateTime yesterday = LocalDateTime.now().minusDays(1);
        return Jwt.of(1L, 1L, "refreshToken", yesterday);
    }

}