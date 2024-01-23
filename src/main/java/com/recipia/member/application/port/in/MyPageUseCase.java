package com.recipia.member.application.port.in;

import com.recipia.member.adapter.in.web.dto.response.FollowingListResponseDto;
import com.recipia.member.adapter.in.web.dto.response.PagingResponseDto;
import com.recipia.member.domain.MyPage;
import org.springframework.web.multipart.MultipartFile;

public interface MyPageUseCase {
    MyPage viewMyPage(Long memberId);
    Long updateMyPage(MyPage myPage, MultipartFile profileImage);
    PagingResponseDto<FollowingListResponseDto> getFollowingList(Long targetMemberId, int page, int size);
}
