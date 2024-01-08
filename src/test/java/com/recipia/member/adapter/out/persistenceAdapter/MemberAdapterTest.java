package com.recipia.member.adapter.out.persistenceAdapter;

import com.recipia.member.adapter.out.persistence.constant.MemberStatus;
import com.recipia.member.common.exception.MemberApplicationException;
import com.recipia.member.config.TotalTestSupport;
import com.recipia.member.domain.Member;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("[통합] 멤버 adapter 테스트")
class MemberAdapterTest extends TotalTestSupport {

    @Autowired
    private MemberAdapter memberAdapter;

    @DisplayName("[happy] 정상적으로 회원 정보를 ID로 찾는 경우")
    @Test
    void findMemberById_HappyPath() {
        // given
        Long validMemberId = 1L; // 가정: 데이터베이스에 존재하는 회원 ID

        // when
        Member result = memberAdapter.findMemberById(validMemberId);

        // then
        assertEquals(validMemberId, result.getId());
    }

    @DisplayName("[bad] 존재하지 않는 회원 ID로 회원 정보를 찾을 경우 예외 발생")
    @Test
    void findMemberById_NotFound() {
        // given
        Long invalidMemberId = 999L; // 가정: 데이터베이스에 존재하지 않는 회원 ID

        // when & then
        assertThrows(MemberApplicationException.class, () -> {
            memberAdapter.findMemberById(invalidMemberId);
        });
    }

    @DisplayName("[happy] DB에 존재하는 ID와 활성화 상태인 회원 찾기 성공")
    @Test
    void findMemberByIdAndStatus_HappyPath() {
        // given
        Long validMemberId = 1L;
        MemberStatus status = MemberStatus.ACTIVE;

        // when
        Member result = memberAdapter.findMemberByIdAndStatus(validMemberId, status);

        // then
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(validMemberId);
        assertThat(result.getStatus()).isEqualTo(status);
    }

    @DisplayName("[happy] DB에 존재하는 이메일로 회원 찾기 성공")
    @Test
    void findMemberByEmail_HappyPath() {
        // given
        String email = "hong1@example.com"; // 데이터베이스에 존재하는 이메일

        // when
        Optional<Member> result = memberAdapter.findMemberByEmail(email);

        // then
        assertTrue(result.isPresent());
        assertThat(result.get().getEmail()).isEqualTo(email);
    }

    @DisplayName("[happy] DB에 존재하는 이메일과 활성화 상태인 회원 찾기 성공")
    @Test
    void findMemberByEmailAndStatus_HappyPath() {
        // given
        String email = "hong1@example.com";
        MemberStatus status = MemberStatus.ACTIVE;

        // when
        Optional<Member> result = memberAdapter.findMemberByEmailAndStatus(email, status);

        // then
        assertTrue(result.isPresent());
        assertThat(result.get().getEmail()).isEqualTo(email);
        assertThat(result.get().getStatus()).isEqualTo(status);
    }

    @DisplayName("[happy] DB에 존재하는 회원 비활성화 성공")
    @Test
    void deactivateMember_HappyPath() {
        // given
        Long memberId = 1L; // 비활성화할 회원의 ID

        // when
        Long updatedCount = memberAdapter.deactivateMember(memberId);

        // then
        assertThat(updatedCount).isGreaterThan(0);
    }

}