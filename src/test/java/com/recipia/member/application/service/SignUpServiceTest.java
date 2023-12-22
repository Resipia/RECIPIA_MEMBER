package com.recipia.member.application.service;

import com.recipia.member.adapter.out.persistence.constant.MemberStatus;
import com.recipia.member.adapter.out.persistence.constant.RoleType;
import com.recipia.member.adapter.out.persistenceAdapter.SignUpAdapter;
import com.recipia.member.domain.Member;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("[단위] 회원가입 Service 테스트")
class SignUpServiceTest {

    @Mock
    private SignUpAdapter signUpAdapterMock;

    @InjectMocks
    private SignUpService sut;

    @DisplayName("[happy] DB에 없는 이메일이 들어왔을때 true를 리턴한다.")
    @Test
    void checkEmailDuplicationTestSuccess() {
        //given
        String email = "test1@gmail.com";

        //when
        when(signUpAdapterMock.isEmailAvailable(email)).thenReturn(true);
        boolean isEmailAvailable = sut.isEmailAvailable(email);

        //then
        assertThat(isEmailAvailable).isTrue();

    }

    @DisplayName("[bad] DB에 이미 존재하는 이메일이 들어왔을때 false를 리턴한다.")
    @Test
    void checkEmailDuplicationTestFail() {
        //given
        String email = "test1@example.com";

        //when
        when(signUpAdapterMock.isEmailAvailable(email)).thenReturn(false);
        boolean isEmailAvailable = sut.isEmailAvailable(email);
        //then
        assertThat(isEmailAvailable).isFalse();
    }

    @DisplayName("[happy] DB에 없는 휴대폰 번호가 들어왔을때 true를 리턴한다.")
    @Test
    void checkTelNoDuplicationTestSuccess() {
        //given
        String telNo = "101-1111-1111";

        //when
        when(signUpAdapterMock.isTelNoAvailable(telNo)).thenReturn(true);
        boolean isTelNoAvailable = sut.isTelNoAvailable(telNo);

        //then
        assertThat(isTelNoAvailable).isTrue();

    }

    @DisplayName("[bad] DB에 이미 존재하는 휴대폰 번호가 들어왔을때 false를 리턴한다.")
    @Test
    void checkTelNoDuplicationTestFail() {
        //given
        String telNo = "010-1234-5678";

        //when
        when(signUpAdapterMock.isTelNoAvailable(telNo)).thenReturn(false);
        boolean isTelNoAvailable = sut.isTelNoAvailable(telNo);

        //then
        assertThat(isTelNoAvailable).isFalse();

    }

    @DisplayName("[happy] 회원가입에 성공한 회원의 아이디값을 리턴한다.")
    @Test
    void signUpTestSuccess() {
        //given
        Member member = createMember();
        //when
        when(signUpAdapterMock.signUpMember(member)).thenReturn(4L);
        Long createdMemberId = sut.signUp(member);

        //then
        assertThat(createdMemberId).isEqualTo(4L);

    }

    private Member createMember() {
        return Member.of(null, "test1@example.com", "$2a$10$ntfXSI6blB139A7azjeS9ep4todVsHMyd95.y1AF6i2mUe.9WBmte", "Full Name 1", "Nickname1",  MemberStatus.ACTIVE, "Introduction 1", "010-1234-5678",
                "Address 1-1", "Address 1-2", "Y", "Y", "Y","Y",RoleType.MEMBER);
    }

}