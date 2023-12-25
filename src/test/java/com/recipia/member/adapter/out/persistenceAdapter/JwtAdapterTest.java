package com.recipia.member.adapter.out.persistenceAdapter;

import com.recipia.member.common.exception.ErrorCode;
import com.recipia.member.common.exception.MemberApplicationException;
import com.recipia.member.config.TotalTestSupport;
import com.recipia.member.domain.Jwt;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

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