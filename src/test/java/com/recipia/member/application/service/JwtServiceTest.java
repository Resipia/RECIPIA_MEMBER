package com.recipia.member.application.service;

import com.recipia.member.adapter.out.persistence.constant.MemberStatus;
import com.recipia.member.adapter.out.persistence.constant.RoleType;
import com.recipia.member.application.port.out.port.JwtPort;
import com.recipia.member.application.port.out.port.MemberPort;
import com.recipia.member.config.jwt.TokenUtils;
import com.recipia.member.domain.Jwt;
import com.recipia.member.domain.Member;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("[단위] JWT Service 테스트")
class JwtServiceTest {

    @InjectMocks
    private JwtService sut;

    @Mock
    private JwtPort jwtPort;

    @Mock
    private MemberPort memberPort;

    @DisplayName("[happy] memberId, refresh token으로 jwt 가져와서 만료기한 안지났으면 access token 리턴 성공")
    @Test
    void republishAccessTokenSuccess() {

        // given
        Jwt jwt = createJwtUsingValidRefresh();
        when(jwtPort.getJwt(any())).thenReturn(jwt);
        Member member = createMember();
        when(memberPort.findMemberByIdAndStatus(any(), any())).thenReturn(member);

        try (MockedStatic<TokenUtils> mockedStatic = mockStatic(TokenUtils.class)) {
            mockedStatic.when(() -> TokenUtils.generateAccessToken(any())).thenReturn("successfully-generate-access-token");

            // when
            String accessToken = sut.republishAccessToken(jwt);

            // then
            mockedStatic.verify(() -> TokenUtils.generateAccessToken(any()));
            assertThat(accessToken).isEqualTo("successfully-generate-access-token");
        }
    }


    public Jwt createJwtUsingValidRefresh() {
        LocalDateTime sixMonthsLater = LocalDateTime.now().plusMonths(6);
        return Jwt.of(1L, 6L, "refreshToken", sixMonthsLater);
    }

    public Jwt createJwtUsingInvalidRefresh() {
        LocalDateTime yesterday = LocalDateTime.now().minusDays(1);
        return Jwt.of(1L, 6L, "refreshToken", yesterday);
    }

    private Member createMember() {
        return Member.of(6L, "test1@example.com", "$2a$10$ntfXSI6blB139A7azjeS9ep4todVsHMyd95.y1AF6i2mUe.9WBmte", "Full Name 1", "Nickname1", MemberStatus.ACTIVE, "Introduction 1", "01012345678",
                "Address 1-1", "Address 1-2", "Y", "Y", "Y", "Y", RoleType.MEMBER);
    }

}