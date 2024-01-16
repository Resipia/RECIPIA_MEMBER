package com.recipia.member.adapter.out.persistenceAdapter;

import com.recipia.member.adapter.out.persistence.MemberFileEntity;
import com.recipia.member.adapter.out.persistence.constant.MemberStatus;
import com.recipia.member.common.exception.MemberApplicationException;
import com.recipia.member.config.TotalTestSupport;
import com.recipia.member.domain.Member;
import com.recipia.member.domain.MemberFile;
import com.recipia.member.domain.MyPage;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("[통합] 멤버 adapter 테스트")
class MemberAdapterTest extends TotalTestSupport {

    @Autowired
    private MemberAdapter sut;
    @Autowired
    private MemberFileRepository memberFileRepository;

    @DisplayName("[happy] 정상적으로 회원 정보를 ID로 찾는 경우")
    @Test
    void findMemberById_HappyPath() {
        // given
        Long validMemberId = 1L; // 가정: 데이터베이스에 존재하는 회원 ID

        // when
        Member result = sut.findMemberById(validMemberId);

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
            sut.findMemberById(invalidMemberId);
        });
    }

    @DisplayName("[happy] DB에 존재하는 ID와 활성화 상태인 회원 찾기 성공")
    @Test
    void findMemberByIdAndStatus_HappyPath() {
        // given
        Long validMemberId = 1L;
        MemberStatus status = MemberStatus.ACTIVE;

        // when
        Member result = sut.findMemberByIdAndStatus(validMemberId, status);

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
        Optional<Member> result = sut.findMemberByEmail(email);

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
        Optional<Member> result = sut.findMemberByEmailAndStatus(email, status);

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
        Long updatedCount = sut.deactivateMember(memberId);

        // then
        assertThat(updatedCount).isGreaterThan(0);
    }

    @DisplayName("[happy] 프로필 이미지 저장에 성공한다.")
    @Test
    void saveMemberFileSuccess() {
        // given
        MemberFile memberFile = MemberFile.of(Member.of(2L), 0, "path", "object-url", "origin", "stored", "jpg", 10, "N");

        // when
        Long savedMemberFileId = sut.saveMemberFile(memberFile);
        // then
        assertThat(savedMemberFileId).isNotNull();
        Optional<MemberFileEntity> savedMemberFile = memberFileRepository.findById(savedMemberFileId);
        assertThat(savedMemberFile.isPresent()).isTrue();
    }

    @DisplayName("[happy] 멤버 프로필 이미지를 soft delete 처리에 성공한다.")
    @Test
    void softDeleteProfileImageSuccess() {
        // given
        MyPage myPage = MyPage.builder().memberId(1L).deleteFileOrder(1).build();
        // when
        Long updatedCount = sut.softDeleteProfileImage(myPage);
        // then
        assertEquals(1L, updatedCount);
    }

    @DisplayName("DB에 존재하는 파일의 max값을 반환한다.")
    @Test
    void findMaxFileOrderByExistingMemberFileSuccess() {
        // given
        Long memberId = 1L;
        // when
        Integer maxFileOrder = sut.findMaxFileOrder(memberId);
        // then
        assertEquals(maxFileOrder, 1);
    }

    @DisplayName("DB에 존재하지 않는 파일의 max값은 0으로 반환한다.")
    @Test
    void findMaxFileOrderByNonExistingMemberFileSuccess() {
        // given
        Long memberId = 2L;
        // when
        Integer maxFileOrder = sut.findMaxFileOrder(memberId);
        // then
        assertEquals(maxFileOrder, 0);
    }

    @DisplayName("[happy] 회원 상태가 전부 ACTIVE인 회원 id 리스트를 주입받았을때 true를 반환한다.")
    @Test
    void isAllMemberActiveSuccess() {
        // given
        List<Long> memberIdList = List.of(1L, 3L);
        // when
        boolean isAllMemberActive = sut.isAllMemberActive(memberIdList);
        // then
        assertTrue(isAllMemberActive);
    }

    @DisplayName("[happy] 회원 상태가 한명은 ACTIVE, 한명은 DORMANT인 리스트를 주입받았을때 false를 반환한다.")
    @Test
    void isOneMemberActiveSuccess() {
        // given
        List<Long> memberIdList = List.of(1L, 2L);
        // when
        boolean isAllMemberActive = sut.isAllMemberActive(memberIdList);
        // then
        assertFalse(isAllMemberActive);
    }
}