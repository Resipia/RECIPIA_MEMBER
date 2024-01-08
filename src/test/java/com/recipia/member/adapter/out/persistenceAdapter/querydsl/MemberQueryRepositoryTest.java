package com.recipia.member.adapter.out.persistenceAdapter.querydsl;

import com.recipia.member.config.TotalTestSupport;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@Transactional
@DisplayName("[통합] 멤버 qeury repository 테스트")
class MemberQueryRepositoryTest extends TotalTestSupport {

    @Autowired
    private MemberQueryRepository sut;

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

}