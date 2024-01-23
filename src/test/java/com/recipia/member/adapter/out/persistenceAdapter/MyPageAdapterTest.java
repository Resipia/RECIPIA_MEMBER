package com.recipia.member.adapter.out.persistenceAdapter;

import com.recipia.member.config.TotalTestSupport;
import com.recipia.member.domain.MyPage;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("[통합] 마이페이지 Adapter 테스트")
class MyPageAdapterTest extends TotalTestSupport {

    @Autowired
    private MyPageAdapter sut;

    @DisplayName("[happy] DB에 존재하는 targetMemberId로 요청왔을때 마이페이지 조회 성공")
    @Test
    void viewMyPageInfoSuccess() {
        // given
        Long memberId = 1L;
        Long targetMemberId = 2L;

        // when
        MyPage result = sut.viewMyPage(memberId, targetMemberId);

        // then
        assertNotNull(result);
        assertEquals(result.getMemberId(), targetMemberId);
    }


    @DisplayName("[happy] 내가 팔로우하고있는 다른 회원의 마이페이지 조회 요청했을때 followId에 데이터가 들어있다.")
    @Test
    void viewFollowingMyPageInfoSuccess() {
        // given
        Long memberId = 1L;
        Long targetMemberId = 2L;

        // when
        MyPage result = sut.viewMyPage(memberId, targetMemberId);

        // then
        assertNotNull(result);
        assertNotNull(result.getFollowId());
    }


    @DisplayName("[bad] DB에 존재하지 않는 targetMemberId로 요청왔을때 마이페이지 조회 실패")
    @Test
    void viewMyPageInfoFail() {
        // given
        Long invalidMemberId = 1L;
        Long targetMemberId = -2L;

        // when
        MyPage result = sut.viewMyPage(invalidMemberId, targetMemberId);

        // then
        assertNull(result);
    }

    @DisplayName("[happy] 마이페이지 수정 성공")
    @Test
    void updateMyPageInfoSuccess() {
        // given
        MyPage requestMyPage = MyPage.of(1L, "update-nickname", "update-introduction");

        // when
        Long updatedCount = sut.updateMyPage(requestMyPage);

        // then
        assertEquals(updatedCount, 1L);
    }

}