package com.recipia.member.adapter.out.persistenceAdapter.querydsl;

import com.recipia.member.adapter.out.persistence.MemberFileEntity;
import com.recipia.member.adapter.out.persistenceAdapter.MemberFileRepository;
import com.recipia.member.config.TotalTestSupport;
import com.recipia.member.domain.MyPage;
import com.recipia.member.domain.TempPassword;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@Transactional
@DisplayName("[통합] 멤버 qeury repository 테스트")
class MemberQueryRepositoryTest extends TotalTestSupport {

    @Autowired
    private MemberQueryRepository sut;
    @Autowired
    private MemberFileRepository memberFileRepository;

    @DisplayName("[happy] memberId가 들어오면 회원 비활성화 업데이트 성공")
    @Test
    void deactivateMemberSuccess() {
        // given
        Long memberId = 1L;
        // when
        Long updatedCount = sut.deactivateMemberByMemberId(memberId);
        // then
        assertEquals(1L, updatedCount); // 하나의 레코드가 업데이트 되었는지 확인
    }

    @DisplayName("[happy] memberId에 해당하는 멤버의 프로필 이미지 soft delete 성공")
    @Test
    void softDeleteMemberFileSuccess() {
        // given
        MyPage myPage = MyPage.builder().memberId(1L).build();

        // when
        sut.softDeleteMemberFile(myPage);
        // then
        MemberFileEntity updatedEntity = memberFileRepository.findByMemberEntity_Id(myPage.getMemberId()).orElseThrow();
        assertEquals("Y", updatedEntity.getDelYn());
    }

    @DisplayName("[happy] 회원 상태가 ACTIVE인 회원 두명을 받았을때 2를 반환한다.")
    @Test
    void findAllMemberByIdAndStatusSuccess() {
        // given
        List<Long> memberIdList = List.of(1L, 3L);
        // when
        Long memberCount = sut.findAllMemberByIdAndStatus(memberIdList);
        // then
        assertEquals(memberIdList.size(), memberCount);
    }

    @DisplayName("[happy] db에 존재하는 회원의 이메일로 비밀번호를 업데이트한다.")
    @Test
    void updatePasswordSuccess() {
        // given
        String email = "hong1@example.com";
        String encryptedTempPassword = "encryptedTemp";
        // when
        Long updatedCount = sut.updatePassword(email, encryptedTempPassword);
        // then
        assertEquals(updatedCount, 1L);
    }
}