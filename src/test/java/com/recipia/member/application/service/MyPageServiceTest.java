package com.recipia.member.application.service;

import com.recipia.member.application.port.out.port.MyPagePort;
import com.recipia.member.domain.MyPage;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("[단위] 마이페이지 서비스 테스트")
class MyPageServiceTest {

    @InjectMocks
    private MyPageService sut;

    @Mock
    private MyPagePort myPagePort;

    @DisplayName("[happy] 정상적인 마이페이지 조회")
    @Test
    void viewMyPageSuccess() {
        // given
        MyPage requestMyPage = MyPage.of(1L);
        MyPage responseMyPage = MyPage.of(1L, "nickname", "intro", 3L, 4L);
        when(myPagePort.viewMyPage(1L)).thenReturn(responseMyPage);

        // when
        MyPage result = sut.viewMyPage(requestMyPage);

        // then
        assertEquals(responseMyPage, result);
    }

    @DisplayName("[bad] 마이페이지 조회 실패")
    @Test
    void viewMyPageFail() {
        // given
        MyPage requestMyPage = MyPage.of(1L);
        when(myPagePort.viewMyPage(anyLong())).thenThrow(new RuntimeException("데이터베이스 오류"));

        // when
        Exception exception = assertThrows(RuntimeException.class,
                () -> sut.viewMyPage(requestMyPage));

        // then
        assertEquals("데이터베이스 오류", exception.getMessage());
    }

}