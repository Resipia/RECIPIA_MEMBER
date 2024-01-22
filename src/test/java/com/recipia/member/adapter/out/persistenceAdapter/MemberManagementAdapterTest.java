package com.recipia.member.adapter.out.persistenceAdapter;

import com.recipia.member.config.TotalTestSupport;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DisplayName("[통합] 회원 관리 Adapter 테스트")
class MemberManagementAdapterTest extends TotalTestSupport {

    @Autowired
    private MemberManagementAdapter sut;

    @DisplayName("[happy] DB에 없는 이메일이 들어왔을때 true를 리턴한다.")
    @Test
    void checkEmailDuplicationTestSuccess() {
        //given
        String email = "test1@gmail.com";

        //when
        boolean isEmailAvailable = sut.isEmailAvailable(email);
        //then
        assertThat(isEmailAvailable).isTrue();
    }

    @DisplayName("[bad] DB에 이미 존재하는 이메일이 들어왔을때 false를 리턴한다.")
    @Test
    void checkEmailDuplicationTestFail() {
        //given
        String email = "hong1@example.com";

        //when
        boolean isEmailAvailable = sut.isEmailAvailable(email);
        //then
        assertThat(isEmailAvailable).isFalse();
    }

    @DisplayName("[happy] DB에 없는 휴대폰 번호가 들어왔을때 true를 리턴한다.")
    @Test
    void checkTelNoDuplicationTestSuccess() {
        //given
        String telNo = "10111111111";

        //when
        boolean isTelNoAvailable = sut.isTelNoAvailable(telNo);

        //then
        assertThat(isTelNoAvailable).isTrue();

    }

    @DisplayName("[bad] DB에 이미 존재하는 휴대폰 번호가 들어왔을때 false를 리턴한다.")
    @Test
    void checkTelNoDuplicationTestFail() {
        //given
        String telNo = "01012345678";

        //when
        boolean isTelNoAvailable = sut.isTelNoAvailable(telNo);

        //then
        assertThat(isTelNoAvailable).isFalse();

    }

    @DisplayName("[happy] db에 존재하지 않는 닉네임을 요청받으면 true를 반환한다.")
    @Test
    void isNicknameAvailableTrue() {
        // given
        String nickname = "hello";
        // when
        boolean isNicknameAvailable = sut.isNicknameAvailable(nickname);
        // then
        assertTrue(isNicknameAvailable);
    }

    @DisplayName("[happy] 탈퇴한 회원의 닉네임을 요청받으면 true를 반환한다.")
    @Test
    void isNicknameAvailableTrueWithDeactiveNickname() {
        // given
        String nickname = "jung5";
        // when
        boolean isNicknameAvailable = sut.isNicknameAvailable(nickname);
        // then
        assertTrue(isNicknameAvailable);
    }

    @DisplayName("[happy] DB에 이미 존재하는 닉네임을 요청받으면 false를 반환한다.")
    @Test
    void isNicknameAvailableFalse() {
        // given
        String nickname = "hong1";
        // when
        boolean isNicknameAvailable = sut.isNicknameAvailable(nickname);
        // then
        assertFalse(isNicknameAvailable);
    }


}