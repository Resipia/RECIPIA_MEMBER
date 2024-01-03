package com.recipia.member.adapter.out.persistenceAdapter;

import com.recipia.member.common.exception.MemberApplicationException;
import com.recipia.member.config.TotalTestSupport;
import com.recipia.member.domain.Follow;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("[통합] Follow Adapter 테스트")
class FollowAdapterTest extends TotalTestSupport {

    @Autowired
    private FollowAdapter sut;

    @DisplayName("[happy] 새로운 팔로우 관계가 생성된다.")
    @Test
    void followSuccess() {
        // given
        Follow follow = Follow.of(1L, 3L);
        // when
        Long savedId = sut.follow(follow);
        // then
        assertNotNull(savedId);
    }

    @DisplayName("[bad] 비활성화 상태의 사용자를 팔로우하는 경우 팔로우 실패한다.")
    @Test
    void followFail() {
        // given
        Follow follow = Follow.of(1L, 2L);
        // when
        Exception exception = assertThrows(MemberApplicationException.class, () -> {
            sut.follow(follow);
        });

        //then
        assertNotNull(exception);

    }
}