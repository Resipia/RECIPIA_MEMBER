package com.recipia.member.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("[단위] 회원 관리 Domain 테스트")
class MemberManagementTest {


    @DisplayName("[happy] 이메일 형식이 맞는 이메일이 들어왔을때 성공한다.")
    @Test
    void validEmailTestSuccess() {
        //given
        String email = "hello@naver.com";

        //when
        boolean isValidEmail = MemberManagement.isValidEmail(email);

        //then
        assertThat(isValidEmail).isTrue();
    }

    @DisplayName("[bad] 이메일 형식이 맞지 않는 이메일이 들어왔을때 실패한다.")
    @Test
    void validEmailTestFail() {
        //given
        String email = "hello.naver.com";

        //when
        boolean isValidEmail = MemberManagement.isValidEmail(email);

        //then
        assertThat(isValidEmail).isFalse();
    }


}