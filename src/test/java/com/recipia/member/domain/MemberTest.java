package com.recipia.member.domain;

import com.recipia.member.adapter.out.persistence.constant.MemberStatus;
import com.recipia.member.adapter.out.persistence.constant.RoleType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("[단위] Member Domain 테스트")
class MemberTest {

    @DisplayName("[happy] 비밀번호 정규식에 맞는 비밀번호가 들어왔을때 성공한다.")
    @Test
    void validPasswordTestSuccess() {
        //given
        String password = "HelloNick2MeetU!";
        Member member = Member.of(1L);
        //when
        boolean isValidPassword = member.isValidPassword(password);

        //then
        assertThat(isValidPassword).isTrue();
    }

    @DisplayName("[happy] 비밀번호 암호화에 성공한다.")
    @Test
    void passwordEncoderSuccess() {
        //given
        Member member = createMember();
        String beforePassword = member.getPassword();

        //when
        member.passwordEncoder();

        //then
        assertThat(beforePassword).isNotEqualTo(member.getPassword());
    }

    @DisplayName("[bad] 비밀번호 정규식에 맞지 않는 비밀번호가 들어왔을때 실패한다.")
    @Test
    void validPasswordTestFail() {
        //given
        String password = "hello!!!!!!!!!";
        Member member = Member.of(1L);

        //when
        boolean isValidPassword = member.isValidPassword(password);

        //then
        assertThat(isValidPassword).isFalse();
    }

    private Member createMember() {
        return Member.of(1L, "test1@example.com", "$2a$10$ntfXSI6blB139A7azjeS9ep4todVsHMyd95.y1AF6i2mUe.9WBmte", "Full Name 1", "Nickname1",  MemberStatus.ACTIVE, "Introduction 1", "01012345678",
                "Address 1-1", "Address 1-2", RoleType.MEMBER);
    }

}