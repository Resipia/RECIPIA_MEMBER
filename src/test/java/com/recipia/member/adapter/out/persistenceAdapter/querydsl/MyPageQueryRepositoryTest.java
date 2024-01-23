package com.recipia.member.adapter.out.persistenceAdapter.querydsl;

import com.recipia.member.adapter.in.web.dto.response.FollowListResponseDto;
import com.recipia.member.config.TotalTestSupport;
import com.recipia.member.domain.MyPage;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("[통합] 마이페이지 쿼리 리포지토리 테스트")
class MyPageQueryRepositoryTest extends TotalTestSupport {

    @Autowired
    private MyPageQueryRepository sut;

    @DisplayName("[happy] 정상적인 마이페이지 조회")
    @Test
    void viewMyPageSuccess() {
        // given
        Long existingMemberId = 1L; // DB에 존재하는 멤버 ID

        // when
        MyPage result = sut.viewMyPage(existingMemberId);

        // then
        assertNotNull(result);
        assertEquals(existingMemberId, result.getMemberId());
    }

    @DisplayName("[bad] 존재하지 않는 사용자로 마이페이지 조회")
    @Test
    void viewMyPageFail() {
        // given
        Long nonExistingMemberId = 0L; // DB에 존재하지 않는 멤버 ID

        // when
        MyPage result = sut.viewMyPage(nonExistingMemberId);

        // then
        assertNull(result);
    }

    @DisplayName("[happy] 회원의 팔로우 목록을 가져온다.")
    @Test
    void getFollowingListSuccess() {
        // given
        Long targetMemberId = 1L;
        Long memberId = 2L;
        String type = "following";
        Pageable pageable = PageRequest.of(0, 5);

        // when
        Page<FollowListResponseDto> result = sut.getFollowingList(targetMemberId, memberId, type, pageable);
        // then
        assertThat(result).isNotNull();
        Assertions.assertThat(result.getContent()).isNotEmpty();

    }

}