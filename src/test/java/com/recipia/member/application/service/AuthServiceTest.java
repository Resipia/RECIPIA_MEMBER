package com.recipia.member.application.service;

import com.recipia.member.adapter.out.aws.TokyoSnsService;
import com.recipia.member.domain.Authentication;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

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

    @DisplayName("[happy] 도메인 번호로 문자 메시지가 성공적으로 발송된다.")
    @Test
    void publishSMSToPhoneNumberSuccess() {
        //given
        Authentication authentication = createAuthentication();
        //when
        sut.verityPhoneNumber(authentication);

        //then
        verify(tokyoSnsService).sendVerificationCode(anyString(), anyString());

    }

    private static Authentication createAuthentication() {
        return Authentication.of("+8201000000000", null);
    }

}