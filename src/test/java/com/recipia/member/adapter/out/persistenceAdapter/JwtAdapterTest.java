package com.recipia.member.adapter.out.persistenceAdapter;

import com.recipia.member.adapter.out.persistence.JwtEntity;
import com.recipia.member.common.exception.ErrorCode;
import com.recipia.member.common.exception.MemberApplicationException;
import com.recipia.member.config.TotalTestSupport;
import com.recipia.member.domain.Jwt;
import com.recipia.member.domain.TokenBlacklist;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

@DisplayName("[통합] Jwt Adapter 테스트")
class JwtAdapterTest extends TotalTestSupport {

    @Autowired
    private JwtAdapter sut;

    @DisplayName("[happy] 파라미터로 받은 Jwt를 기반으로 DB에서 해당 Jwt 가져오기 성공")
    @Test
    void getJwtSuccess() {
        //given
        Jwt jwt = createJwt();
        //when
        Jwt dbJwt = sut.getJwt(jwt);
        //then
        assertThat(dbJwt).isNotNull();
        assertThat(dbJwt.getId()).isEqualTo(1L);
    }

    @DisplayName("[bad] 파라미터로 받은 Jwt를 기반으로 DB에 해당되는 JWT 없을때 JWT_NOT_FOUND 에러 발생")
    @Test
    void getJwtFail() {
        //given
        Jwt jwt = createNoneExistJwt();
        // when & then
        assertThatThrownBy(() -> sut.getJwt(jwt))
                .isInstanceOf(MemberApplicationException.class)
                .hasMessageContaining(ErrorCode.JWT_NOT_FOUND.getMessage());
    }


    @DisplayName("[happy] memberId에 해당하는 리프레시 토큰 삭제 성공")
    @Test
    void deleteRefreshTokenSuccess() {
        // given
        Long memberId = 1L; // 존재하는 memberId 가정
        LocalDateTime expirationTime = LocalDateTime.now().plusMinutes(30);
        Jwt jwt = Jwt.of(1L, "some-refresh-token", expirationTime);

        // when
        sut.deleteRefreshToken(memberId);

        // then
        Exception exception = assertThrows(MemberApplicationException.class, () -> {
            sut.getJwt(jwt);
        });

        // then
        // 예외가 실제로 발생했는지 확인한다. 예외 객체가 null이 아니어야 한다.
        assertNotNull(exception);

    }

    @DisplayName("[happy] TokenBlacklist 데이터베이스에 추가 성공")
    @Test
    void insertTokenBlacklistSuccess() {
        // given
        LocalDateTime expirationTime = LocalDateTime.now().plusMinutes(30);
        TokenBlacklist tokenBlacklist = TokenBlacklist.of("accessTokne", expirationTime);

        // when
        sut.insertTokenBlacklist(tokenBlacklist);

        // then
        TokenBlacklist dbTokenBlacklist = sut.getTokenBlacklist(tokenBlacklist);
        assertThat(dbTokenBlacklist).isNotNull();
        assertThat(dbTokenBlacklist.getToken()).isEqualTo(tokenBlacklist.getToken());
    }

    private Jwt createJwt() {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime sixMonthsLater = now.plusMonths(6);

        String refreshToken = "some-refresh-token";

        return Jwt.of(1L, 1L, refreshToken, sixMonthsLater);
    }

    private Jwt createNoneExistJwt() {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime sixMonthsLater = now.plusMonths(6);

        String refreshToken = "non-exist-refresh-token";

        return Jwt.of(1L, 1L, refreshToken, sixMonthsLater);
    }

}