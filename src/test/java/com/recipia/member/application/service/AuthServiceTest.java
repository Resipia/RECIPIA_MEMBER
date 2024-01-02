package com.recipia.member.application.service;

import com.recipia.member.adapter.out.aws.TokyoSnsService;
import com.recipia.member.domain.Authentication;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;

import static org.junit.jupiter.api.Assertions.*;
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


    @DisplayName("[happy] 도메인 번호로 문자 메시지가 성공적으로 발송된다.")
    @Test
    void publishSMSToPhoneNumberSuccess() {
        //given
        Authentication authentication = createAuthenticationWoCode();
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
        when(redisService.getValues(authentication.getPhoneNumber())).thenReturn("123456");
        //when
        boolean isVerifyCodeEqual = sut.checkVerifyCode(authentication);
        //then
        assertTrue(isVerifyCodeEqual);
    }

    private static Authentication createAuthenticationWoCode() {
        return Authentication.of("+8201000000000", null, null);
    }

    private static Authentication createAuthenticationWCode() {
        return Authentication.of("+8201000000000", null, "123456");
    }

}