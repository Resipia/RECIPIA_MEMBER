package com.recipia.member.application.service;

import com.recipia.member.domain.Jwt;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("[단위] JWT Service 테스트")
class JwtServiceTest {

    @InjectMocks
    private JwtService sut;

    @DisplayName("[happy] ")
    @Test
    void test() {
        //given
        Jwt jwt = createJwt();
        //when
        String accessToken = sut.republishAccessToken(jwt);
        //then
        assertThat(accessToken).isNotNull();
        assertThat(accessToken).isEqualTo("1234");

    }

    private Jwt createJwt() {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime sixMonthsLater = now.plusMonths(6);

        String refreshToken = "some-refresh-token";

        return Jwt.of(1L, refreshToken, sixMonthsLater);
    }

}