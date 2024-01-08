package com.recipia.member.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

@DisplayName("[단위] 본인 인증 도메인 테스트")
class AuthenticationTest {

    @DisplayName("[happy] 사용자가 입력한 전화번호를 +8201000000000 형태로 변환한다.")
    @Test
    void formatPhoneNumberSuccess() {
        //given
        Authentication authentication = createAuthentication();
        String beforePhoneNumber = authentication.getPhoneNumber();
        String afterPhoneNumber = "+8201000001111";

        //when
        authentication.formatPhoneNumber();

        //then
        assertThat(beforePhoneNumber).isNotEqualTo(authentication.getPhoneNumber());
        assertEquals(authentication.getPhoneNumber(), afterPhoneNumber);

    }

    private static Authentication createAuthentication() {
        return Authentication.of("        01000001111   ", null, null);
    }

}