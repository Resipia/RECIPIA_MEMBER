package com.recipia.member.application.service;

import com.recipia.member.application.port.out.port.FollowPort;
import com.recipia.member.common.exception.MemberApplicationException;
import com.recipia.member.domain.Follow;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("[단위] 팔로우 Service 테스트")
class FollowServiceTest {

    @InjectMocks
    private FollowService sut;

    @Mock
    private FollowPort followPort;

    @DisplayName("[happy] 서로 팔로우 관계가 아닌 상태에서 팔로우 요청이 오면 팔로우 성공")
    @Test
    void followRequestSuccess() {
        //given
        Follow follow = Follow.of(1L, 2L);
        when(followPort.existsFollowRelation(follow)).thenReturn(false);
        when(followPort.follow(follow)).thenReturn(1L);

        //when
        Long savedFollowId = sut.followRequest(follow);

        //then
        assertNotNull(savedFollowId);
        assertEquals(1L, savedFollowId);
        verify(followPort, times(1)).follow(follow);
    }

    @DisplayName("[bad] 이미 팔로우 관계인 상태에서 팔로우 해제 성공")
    @Test
    void unfollowRequestSuccess() {
        // given
        Follow follow = Follow.of(1L, 2L);
        when(followPort.existsFollowRelation(follow)).thenReturn(true);
        when(followPort.unfollow(follow)).thenReturn(1L);

        // when
        Long deletedCount = sut.followRequest(follow);

        // then
        assertThat(deletedCount).isEqualTo(0L);
    }


}