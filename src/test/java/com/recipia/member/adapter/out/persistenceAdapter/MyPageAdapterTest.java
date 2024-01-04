package com.recipia.member.adapter.out.persistenceAdapter;

import com.recipia.member.common.exception.MemberApplicationException;
import com.recipia.member.config.TotalTestSupport;
import com.recipia.member.domain.MyPage;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.jupiter.api.Assertions.*;
import static reactor.core.publisher.Mono.when;

@DisplayName("[통합] 마이페이지 Adapter 테스트")
class MyPageAdapterTest extends TotalTestSupport {

    @Autowired
    private MyPageAdapter sut;

    @DisplayName("[happy] DB에 존재하는 memberId로 요청왔을때 마이페이지 조회 성공")
    @Test
    void viewMyPageInfoSuccess() {
        // given
        Long memberId = 1L; // 예시로 사용하는 멤버 ID

        // when
        MyPage result = sut.viewMyPage(memberId);

        // then
        assertNotNull(result);
        assertEquals(memberId, result.getMemberId());
    }

    @DisplayName("[bad] DB에 존재하지 않는 memberId로 요청왔을때 마이페이지 조회 실패")
    @Test
    void viewMyPageInfoFail() {
        // given
        Long invalidMemberId = -1L; // 존재하지 않는 멤버 ID

        // when
        MyPage result = sut.viewMyPage(invalidMemberId);

        // then
        assertNull(result);
    }

    @DisplayName("[happy] 마이페이지 수정 성공")
    @Test
    void updateMyPageInfoSuccess() {
        // given
        MyPage requestMyPage = MyPage.of(1L, "update-nickname", "update-intro");

        // when
        Long updatedCount = sut.updateMyPage(requestMyPage);

        // then
        assertEquals(updatedCount, 1L);
    }

}