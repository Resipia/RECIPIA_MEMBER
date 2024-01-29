package com.recipia.member.application.service;

import com.recipia.member.adapter.in.web.dto.response.FollowListResponseDto;
import com.recipia.member.adapter.in.web.dto.response.PagingResponseDto;
import com.recipia.member.adapter.out.persistence.constant.MemberStatus;
import com.recipia.member.adapter.out.persistence.constant.RoleType;
import com.recipia.member.application.port.out.port.MemberPort;
import com.recipia.member.application.port.out.port.MyPagePort;
import com.recipia.member.common.event.NicknameChangeSpringEvent;
import com.recipia.member.common.exception.MemberApplicationException;
import com.recipia.member.common.utils.SecurityUtils;
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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

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
    @Mock
    private SecurityUtils securityUtils;


    @DisplayName("[happy] 프로필 이미지가 있는 유저의 마이페이지 조회")
    @Test
    void viewMyPageWithProfileImageSuccess() {
        // given
        Long memberId = 1L;
        Long targetMemberId = 2L;
        MyPage beforePreSignedResponse = MyPage.builder().memberId(memberId).imageFilePath("file/path").nickname("nickname").followerCount(3L).followingCount(54L).build();
        String expectedPreSignedUrl = "pre-signed-url";
        when(memberPort.isAllMemberActive(anyList())).thenReturn(true);
        when(myPagePort.viewMyPage(memberId, targetMemberId)).thenReturn(beforePreSignedResponse);
        when(imageS3Service.generatePreSignedUrl(beforePreSignedResponse.getImageFilePath(), 60)).thenReturn("pre-signed-url");

        // when
        MyPage result = sut.viewMyPage(memberId, targetMemberId);

        // then
        assertNotNull(result);
        assertEquals(memberId, result.getMemberId());
        assertEquals(beforePreSignedResponse.getNickname(), result.getNickname());
        assertEquals(expectedPreSignedUrl, result.getImagePreUrl());
    }


    @DisplayName("[happy] 프로필 이미지가 없는 유저의 마이페이지 조회")
    @Test
    void viewMyPageWithoutProfileImageSuccess() {
        // given
        Long memberId = 1L;
        Long targetMemberId = 2L;
        MyPage myPage = MyPage.of(memberId, null, "nickname", "introduction", 3L, 4L, "2020-02-02", "M");
        when(memberPort.isAllMemberActive(anyList())).thenReturn(true);
        when(myPagePort.viewMyPage(memberId, targetMemberId)).thenReturn(myPage);

        // when
        MyPage result = sut.viewMyPage(memberId, targetMemberId);

        // then
        assertNotNull(result);
        assertEquals(memberId, result.getMemberId());
        assertEquals("nickname", result.getNickname());
        assertEquals("introduction", result.getIntroduction());

        // 이미지 파일이 없기때문에 imageS3Service.generatePreSignedUrl 메서드가 한번도 호출되지 않았음을 검증
        verify(imageS3Service, never()).generatePreSignedUrl(anyString(), anyInt());

    }

    @DisplayName("[bad] 데이터베이스 오류로 마이페이지 조회 실패")
    @Test
    void viewMyPageFail() {
        // given
        Long memberId = 1L;
        Long targetMemberId = 2L;
        when(memberPort.isAllMemberActive(anyList())).thenReturn(true);
        when(myPagePort.viewMyPage(anyLong(), anyLong())).thenThrow(new RuntimeException("데이터베이스 오류"));

        // when
        Exception exception = assertThrows(RuntimeException.class,
                () -> sut.viewMyPage(memberId, targetMemberId));

        // then
        assertEquals("데이터베이스 오류", exception.getMessage());
    }

    @DisplayName("[happy] 프로필 이미지가 없고, 삭제된 이미지도 없는 상황에서 기본정보만 수정을 요청한 정상적인 마이페이지 수정 성공")
    @Test
    void updateMyPageWithoutProfileImage() {
        // given
        MyPage myPage = MyPage.builder().memberId(1L).nickname("nickname").introduction("intro").build();
        Member member = createMember();
        when(memberPort.findMemberByIdAndStatus(myPage.getMemberId(), MemberStatus.ACTIVE)).thenReturn(member);
        when(myPagePort.updateMyPage(myPage)).thenReturn(1L);

        // when
        Long updatedCount = sut.updateMyPage(myPage, null);

        // then
        assertEquals(updatedCount, 1L);
        verify(memberPort, never()).softDeleteProfileImage(any(MyPage.class));
        verify(imageS3Service, never()).createMemberFile(any(MultipartFile.class), anyLong());
        verify(memberPort, never()).saveMemberFile(any(MemberFile.class));
    }

    @DisplayName("[happy] 프로필 이미지가 없고, 삭제된 이미지가 있는 상황에서 정상적인 마이페이지 수정 성공")
    @Test
    void updateMyPageWithDeleteProfileImage() {
        // given
        MyPage myPage = MyPage.builder().memberId(1L).nickname("nickname").introduction("intro").build();
        Member member = createMember();
        when(memberPort.findMemberByIdAndStatus(myPage.getMemberId(), MemberStatus.ACTIVE)).thenReturn(member);
        when(myPagePort.updateMyPage(myPage)).thenReturn(1L);

        // when
        Long updatedCount = sut.updateMyPage(myPage, null);

        // then
        assertEquals(updatedCount, 1L);
        verify(imageS3Service, never()).createMemberFile(any(MultipartFile.class), anyLong());
        verify(memberPort, never()).saveMemberFile(any(MemberFile.class));
    }


    @DisplayName("[happy] 프로필 이미지가 있고 삭제하는 이미지가 있는 상황에서 정상적인 마이페이지 수정 성공")
    @Test
    void updateMyPageWithProfileImage() {
        // given
        MyPage myPage = MyPage.of(1L, "nickname", "intro");
        MockMultipartFile mockImage = new MockMultipartFile(
                "image", "test.jpg", "image/jpeg", "test image content".getBytes());
        Member member = createMember();
        MemberFile memberFile = MemberFile.of(1L, member, "path", "object-url", "origin", "stored", "jpg", 10, "N");

        when(memberPort.findMemberByIdAndStatus(myPage.getMemberId(), MemberStatus.ACTIVE)).thenReturn(member);
        when(myPagePort.updateMyPage(myPage)).thenReturn(1L);
        when(imageS3Service.createMemberFile(mockImage, myPage.getMemberId())).thenReturn(memberFile);
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
        MemberFile memberFile = MemberFile.of(1L, member,  "path", "object-url", "origin", "stored", "jpg", 10, "N");

        when(memberPort.findMemberByIdAndStatus(myPage.getMemberId(), MemberStatus.ACTIVE)).thenReturn(member);
        when(myPagePort.updateMyPage(myPage)).thenReturn(1L);
        when(imageS3Service.createMemberFile(mockImage, myPage.getMemberId())).thenReturn(memberFile);
        when(memberPort.saveMemberFile(memberFile)).thenReturn(0L);

        // when % then
        assertThrows(MemberApplicationException.class, () -> {
            sut.updateMyPage(myPage, mockImage);
        });

    }

    @DisplayName("[happy] 닉네임이 수정되지 않았을 때 eventPublisher 호출 안 함")
    @Test
    void updateMyPageWithUnchangedNickname() {
        // given
        MyPage myPage = MyPage.builder().memberId(1L).nickname("OldNickname").introduction("intro").build();
        Member member = createMemberWithNickname("OldNickname");
        when(memberPort.findMemberByIdAndStatus(myPage.getMemberId(), MemberStatus.ACTIVE)).thenReturn(member);
        when(myPagePort.updateMyPage(myPage)).thenReturn(1L);

        // when
        sut.updateMyPage(myPage, null);

        // then
        verify(eventPublisher, never()).publishEvent(any(NicknameChangeSpringEvent.class));
    }

    @DisplayName("[happy] 닉네임 수정 시 eventPublisher 호출")
    @Test
    void updateMyPageWithChangedNickname() {
        // given
        MyPage myPage = MyPage.builder().memberId(1L).nickname("NewNickname").introduction("intro").build();
        Member member = createMemberWithNickname("OldNickname");
        when(memberPort.findMemberByIdAndStatus(myPage.getMemberId(), MemberStatus.ACTIVE)).thenReturn(member);
        when(myPagePort.updateMyPage(myPage)).thenReturn(1L);

        // when
        sut.updateMyPage(myPage, null);

        // then
        verify(eventPublisher, times(1)).publishEvent(any(NicknameChangeSpringEvent.class));
    }

    @DisplayName("[happy] 기본 페이징으로 팔로우 목록을 정상적으로 가져온다.")
    @Test
    void getFollowingListSuccess() {
        // given
        int page = 0;
        int size = 10;
        Long targetMemberId = 1L;
        Long loggedMemberId = 2L;
        String type = "follow";
        List<FollowListResponseDto> followingList = List.of(FollowListResponseDto.of(1L, "pre", "nickname", null, false));
        Page<FollowListResponseDto> mockPage = new PageImpl<>(followingList);
        when(securityUtils.getCurrentMemberId()).thenReturn(loggedMemberId);
        when(myPagePort.getFollowList(anyLong(), anyLong(), anyString(), any(Pageable.class))).thenReturn(mockPage);
        // when
        PagingResponseDto<FollowListResponseDto> result = sut.getFollowList(targetMemberId, type, page, size);
        // then
        assertThat(result.getContent()).hasSize(1);
        assertThat(result.getTotalCount()).isEqualTo(1);
    }

    private Member createMemberWithNickname(String nickname) {
        return Member.of(1L, "test@example.com", "password", "Full Name", nickname, MemberStatus.ACTIVE, "Introduction", "01012345678", "Address 1-1", "Address 1-2", RoleType.MEMBER, "Y", "Y", "2020-02-02", "M");
    }


    private Member createMember() {
        return Member.of(1L, "test1@example.com", "$2a$10$ntfXSI6blB139A7azjeS9ep4todVsHMyd95.y1AF6i2mUe.9WBmte", "Full Name 1", "Nickname1", MemberStatus.ACTIVE, "Introduction 1", "01012345678",
                "Address 1-1", "Address 1-2", RoleType.MEMBER, "Y", "Y", "2020-02-02", "M");
    }

}