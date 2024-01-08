package com.recipia.member.application.service;

import com.recipia.member.adapter.out.aws.TokyoSnsService;
import com.recipia.member.application.port.out.port.JwtPort;
import com.recipia.member.application.port.out.port.MemberPort;
import com.recipia.member.domain.Authentication;
import com.recipia.member.domain.Logout;
import com.recipia.member.domain.TokenBlacklist;
import com.recipia.member.domain.converter.JwtConverter;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("[단위] 본인 인증 service 테스트")
class AuthServiceTest {

    @InjectMocks
    private AuthService sut;
    @Mock
    private TokyoSnsService tokyoSnsService;
    @Mock
    private RedisService redisService;
    @Mock
    private ApplicationEventPublisher eventPublisher;
    @Mock
    private MemberManagementService memberManagementService;
    @Mock
    private JwtConverter jwtConverter;
    @Mock
    private JwtPort jwtPort;
    @Mock
    private MemberPort memberPort;



    @DisplayName("[happy] 도메인 번호로 문자 메시지가 성공적으로 발송된다.")
    @Test
    void publishSMSToPhoneNumberSuccess() {
        //given
        Authentication authentication = createAuthenticationWoCode();
        when(memberManagementService.isTelNoAvailable(authentication.getPhoneNumber())).thenReturn(true);
        //when
        sut.verifyPhoneNumber(authentication);

        //then
        verify(tokyoSnsService).sendVerificationCode(anyString(), anyString());

    }

    @DisplayName("[happy] 사용자 전화번호를 레디스에서 검색해서 value로 나온 인증코드와, 사용자가 입력한 인증코드가 일치하는지 검증한다.")
    @Test
    void checkVerifyCodeSuccess() {
        //given
        Authentication authentication = createAuthenticationWCode();
        authentication.formatPhoneNumber();

        when(redisService.getValues(any())).thenReturn("123456"); // 인자 값 일치
        //when
        boolean isVerifyCodeEqual = sut.checkVerifyCode(authentication);
        //then
        assertTrue(isVerifyCodeEqual);
    }

    @DisplayName("[happy] 로그아웃 성공")
    @Test
    void logoutSuccess() {
        // given
        Logout logout = Logout.of(1L, "accessToken");
        LocalDateTime expirationTime = LocalDateTime.now().plusMinutes(30);
        TokenBlacklist tokenBlacklist = TokenBlacklist.of("accessToken", expirationTime);
        when(jwtConverter.logoutToTokenBlacklist(logout)).thenReturn(tokenBlacklist);
        // when
        sut.logout(logout);
        // then
        verify(jwtPort).deleteRefreshToken(logout.getMemberId());
        verify(jwtPort).insertTokenBlacklist(tokenBlacklist);
    }

    @DisplayName("[happy] memberId가 들어왔을때 회원 탈퇴가 성공한다.")
    @Test
    void deactivateMemberSuccess() {
        // given
        Long memberId = 1L;
        when(memberPort.deactivateMember(memberId)).thenReturn(1L);
        // when
        Long updatedCount = sut.deactivateMember(memberId);
        // then
        assertEquals(updatedCount, 1L);
    }

    private static Authentication createAuthenticationWoCode() {
        return Authentication.of("01000000000", null, null);
    }

    private static Authentication createAuthenticationWCode() {
        return Authentication.of("01000000000", null, "123456");
    }

}