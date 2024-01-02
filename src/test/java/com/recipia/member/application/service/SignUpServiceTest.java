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
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("[단위] 회원가입 Service 테스트")
class SignUpServiceTest {

    @Mock
    private SignUpAdapter signUpAdapterMock;

    @InjectMocks
    private SignUpService sut;

    @DisplayName("[happy] 회원가입에 성공한 회원의 아이디값을 리턴한다.")
    @Test
    void signUpTestSuccess() {
        //given
        Member member = createMember();
        //when
        when(signUpAdapterMock.signUpMember(any())).thenReturn(6L);
        Long createdMemberId = sut.signUp(member);

        //then
        assertThat(createdMemberId).isEqualTo(6L);
        assertThat(member.getPassword()).isNotEqualTo("asdfASDF12#");
    }

    private Member createMember() {
        return Member.of(null, "test1@example.com", "asdfASDF12#", "Full Name 1", "Nickname1",  MemberStatus.ACTIVE, "Introduction 1", "01012345678",
                "Address 1-1", "Address 1-2", "Y", "Y", "Y","Y",RoleType.MEMBER);
    }

}