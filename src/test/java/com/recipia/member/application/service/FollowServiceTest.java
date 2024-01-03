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

    @DisplayName("[bad] 이미 팔로우 관계인 상태에서 팔로우 요청이 오면 팔로우 실패")
    @Test
    void followRequestFail() {
        // given
        Follow follow = Follow.of(1L, 2L);
        when(followPort.existsFollowRelation(follow)).thenReturn(true);

        // when
        // followRequest 메서드를 실행할 때 MemberApplicationException 예외가 발생하는지 검증한다.
        // 이 예외는 이미 존재하는 팔로우 관계를 시도할 때 발생해야 한다.
        Exception exception = assertThrows(MemberApplicationException.class, () -> {
            sut.followRequest(follow);
        });

        // then
        // 예외가 실제로 발생했는지 확인한다. 예외 객체가 null이 아니어야 한다.
        assertNotNull(exception);

        // followPort의 follow 메서드가 호출되지 않았음을 검증한다.
        // 이미 팔로우 관계가 있기 때문에, follow 메서드가 호출되면 안된다.
        verify(followPort, never()).follow(follow);
    }


}