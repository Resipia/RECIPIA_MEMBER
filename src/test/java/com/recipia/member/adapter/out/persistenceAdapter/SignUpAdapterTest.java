package com.recipia.member.adapter.out.persistenceAdapter;

import com.recipia.member.adapter.out.persistence.constant.MemberStatus;
import com.recipia.member.adapter.out.persistence.constant.RoleType;
import com.recipia.member.config.TotalTestSupport;
import com.recipia.member.domain.Member;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("[통합] 회원가입 Adapter 테스트")
class SignUpAdapterTest extends TotalTestSupport {

    @Autowired
    private SignUpAdapter sut;

    @DisplayName("[happy] 신규 회원이 회원가입에 성공한다.")
    @Test
    void signUpSuccess() {
        //given
        Member member = createMemberNonExist();
        //when
        Long createdMemberId = sut.signUpMember(member);

        //then
        assertThat(createdMemberId).isNotNull();
    }

    private Member createMemberNonExist() {
        return Member.of(null, "test6@example.com", "$2a$10$ntfXSI6blB139A7azjeS9ep4todVsHMyd95.y1AF6i2mUe.9WBmte", "Full Name 6", "Nickname6", MemberStatus.ACTIVE, "Introduction 6", "01003930303",
                "Address 6-6", "Address 6-2", RoleType.MEMBER);
    }
}