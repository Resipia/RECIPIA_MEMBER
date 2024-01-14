package com.recipia.member.application.service;

import com.recipia.member.adapter.out.persistence.constant.MemberStatus;
import com.recipia.member.adapter.out.persistence.constant.RoleType;
import com.recipia.member.application.port.out.port.MemberPort;
import com.recipia.member.application.port.out.port.MyPagePort;
import com.recipia.member.common.exception.MemberApplicationException;
import com.recipia.member.domain.Member;
import com.recipia.member.domain.MemberFile;
import com.recipia.member.domain.MyPage;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
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
    private ImageS3Service imageS3Service;
    @Mock
    private ApplicationEventPublisher eventPublisher;


    @DisplayName("[happy] 정상적인 마이페이지 조회")
    @Test
    void viewMyPageSuccess() {
        // given
        Long memberId = 1L;
        MyPage beforePreSignedResponse = MyPage.of(1L, "file/path", "nickname", "introduction", 3L, 4L);
        String expectedPreSignedUrl = "pre-signed-url";
        when(myPagePort.viewMyPage(eq(memberId))).thenReturn(beforePreSignedResponse);
        when(imageS3Service.generatePreSignedUrl(eq(beforePreSignedResponse.getProfileImageFilePath()), eq(60))).thenReturn("pre-signed-url");

        // when
        MyPage result = sut.viewMyPage(memberId);

        // then
        assertNotNull(result);
        assertEquals(memberId, result.getMemberId());
        assertEquals("nickname", result.getNickname());
        assertEquals("introduction", result.getIntroduction());
        assertEquals(expectedPreSignedUrl, result.getProfileImageUrl());

    }

    @DisplayName("[bad] 마이페이지 조회 실패")
    @Test
    void viewMyPageFail() {
        // given
        Long memberId = 1L;
        when(myPagePort.viewMyPage(anyLong())).thenThrow(new RuntimeException("데이터베이스 오류"));

        // when
        Exception exception = assertThrows(RuntimeException.class,
                () -> sut.viewMyPage(memberId));

        // then
        assertEquals("데이터베이스 오류", exception.getMessage());
    }

    @DisplayName("[happy] 프로필 이미지가 없는 상황에서 정상적인 마이페이지 수정 성공")
    @Test
    void updateMyPageWithoutProfileImage() {
        // given
        MyPage myPage = MyPage.of(1L, "nickname", "intro");
        Member member = createMember();
        when(memberPort.findMemberByIdAndStatus(myPage.getMemberId(), MemberStatus.ACTIVE)).thenReturn(member);
        when(myPagePort.updateMyPage(myPage)).thenReturn(1L);

        // when
        Long updatedCount = sut.updateMyPage(myPage, null);

        // then
        assertEquals(updatedCount, 1L);
    }


    @DisplayName("[happy] 프로필 이미지가 있는 상황에서 정상적인 마이페이지 수정 성공")
    @Test
    void updateMyPageWithProfileImage() {
        // given
        MyPage myPage = MyPage.of(1L, "nickname", "intro");
        MockMultipartFile mockImage = new MockMultipartFile(
                "image", "test.jpg", "image/jpeg", "test image content".getBytes());
        Member member = createMember();
        MemberFile memberFile = MemberFile.of(1L, member, 0, "path", "object-url", "origin", "stored", "jpg", 10, "N");

        when(memberPort.findMemberByIdAndStatus(myPage.getMemberId(), MemberStatus.ACTIVE)).thenReturn(member);
        when(myPagePort.updateMyPage(myPage)).thenReturn(1L);
        when(imageS3Service.createMemberFile(mockImage, 0, myPage.getMemberId())).thenReturn(memberFile);
        when(memberPort.saveMemberFile(memberFile)).thenReturn(2L);

        // when
        Long updatedCount = sut.updateMyPage(myPage, mockImage);

        // then
        assertEquals(updatedCount, 1L);
    }

    @DisplayName("[bad] DB에러로 인한 마이페이지 수정 실패")
    @Test
    void updateMyPageFail() {
        // given
        MyPage myPage = MyPage.of(1L, "nickname", "intro");
        MockMultipartFile mockImage = new MockMultipartFile(
                "image", "test.jpg", "image/jpeg", "test image content".getBytes());
        Member member = createMember();
        MemberFile memberFile = MemberFile.of(1L, member, 0, "path", "object-url", "origin", "stored", "jpg", 10, "N");

        when(memberPort.findMemberByIdAndStatus(myPage.getMemberId(), MemberStatus.ACTIVE)).thenReturn(member);
        when(myPagePort.updateMyPage(myPage)).thenReturn(1L);
        when(imageS3Service.createMemberFile(mockImage, 0, myPage.getMemberId())).thenReturn(memberFile);
        when(memberPort.saveMemberFile(memberFile)).thenReturn(0L);

        // when % then
        assertThrows(MemberApplicationException.class, () -> {
            sut.updateMyPage(myPage, mockImage);
        });

    }


    private Member createMember() {
        return Member.of(1L, "test1@example.com", "$2a$10$ntfXSI6blB139A7azjeS9ep4todVsHMyd95.y1AF6i2mUe.9WBmte", "Full Name 1", "Nickname1", MemberStatus.ACTIVE, "Introduction 1", "01012345678",
                "Address 1-1", "Address 1-2", RoleType.MEMBER);
    }

}