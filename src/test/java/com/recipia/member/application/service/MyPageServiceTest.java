package com.recipia.member.application.service;

import com.recipia.member.adapter.out.persistence.constant.MemberStatus;
import com.recipia.member.adapter.out.persistence.constant.RoleType;
import com.recipia.member.application.port.out.port.MemberPort;
import com.recipia.member.application.port.out.port.MyPagePort;
import com.recipia.member.common.exception.MemberApplicationException;
import com.recipia.member.domain.Member;
import com.recipia.member.domain.MyPage;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("[단위] 마이페이지 서비스 테스트")
class MyPageServiceTest {

    @InjectMocks
    private MyPageService sut;

    @Mock
    private MyPagePort myPagePort;

    @Mock
    private MemberPort memberPort;

    @Mock
    private ApplicationEventPublisher eventPublisher;


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

    @DisplayName("[happy] 정상적인 마이페이지 수정")
    @Test
    void updateAndViewMyPageSuccess() {
        // given
        MyPage requestMyPage = MyPage.of(1L, "update-nickname", "update-intro");
        MyPage responseMyPage = MyPage.of(1L, "update-nickname", "update-intro", 3L, 4L);
        Member member = createMember();

        when(myPagePort.updateMyPage(requestMyPage)).thenReturn(1L);
        when(myPagePort.viewMyPage(requestMyPage.getMemberId())).thenReturn(responseMyPage);
        when(memberPort.findMemberByIdAndStatus(anyLong(), any(MemberStatus.class))).thenReturn(member);

        // when
        MyPage result = sut.updateAndViewMyPage(requestMyPage);

        // then
        assertEquals(result.getMemberId(), requestMyPage.getMemberId());
        assertEquals(result.getIntroduction(), requestMyPage.getIntroduction());

    }

    @DisplayName("[bad] DB에러로 인한 마이페이지 수정 실패")
    @Test
    void updateAndViewMyPageFail() {
        // given
        MyPage requestMyPage = MyPage.of(1L, "update-nickname", "update-intro");
        Member member = createMember();

        // DB 에러 시뮬레이션: 업데이트가 실패하여 0을 반환
        when(myPagePort.updateMyPage(requestMyPage)).thenReturn(0L);
        when(memberPort.findMemberByIdAndStatus(anyLong(), any(MemberStatus.class))).thenReturn(member);

        // when & then
        assertThrows(MemberApplicationException.class, () -> {
            sut.updateAndViewMyPage(requestMyPage);
        });
    }


    private Member createMember() {
        return Member.of(1L, "test1@example.com", "$2a$10$ntfXSI6blB139A7azjeS9ep4todVsHMyd95.y1AF6i2mUe.9WBmte", "Full Name 1", "Nickname1",  MemberStatus.ACTIVE, "Introduction 1", "01012345678",
                "Address 1-1", "Address 1-2", "Y", "Y", "Y","Y", RoleType.MEMBER);
    }

}