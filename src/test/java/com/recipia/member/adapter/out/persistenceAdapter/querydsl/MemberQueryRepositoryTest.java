package com.recipia.member.adapter.out.persistenceAdapter.querydsl;

import com.recipia.member.adapter.out.persistence.MemberFileEntity;
import com.recipia.member.adapter.out.persistenceAdapter.MemberFileRepository;
import com.recipia.member.config.TotalTestSupport;
import com.recipia.member.domain.MyPage;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

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
        MyPage myPage = MyPage.builder().memberId(1L).deleteFileOrder(1).build();

        // when
        sut.softDeleteMemberFile(myPage);
        // then
        MemberFileEntity updatedEntity = memberFileRepository.findByMemberEntity_Id(myPage.getMemberId()).orElseThrow();
        assertEquals("Y", updatedEntity.getDelYn());
    }

}