package com.recipia.member.application.port.in;

import com.recipia.member.adapter.in.web.dto.response.FollowListResponseDto;
import com.recipia.member.adapter.in.web.dto.response.PagingResponseDto;
import com.recipia.member.domain.MyPage;
import org.springframework.web.multipart.MultipartFile;

public interface MyPageUseCase {
    MyPage viewMyPage(Long memberId);
    Long updateMyPage(MyPage myPage, MultipartFile profileImage);
    PagingResponseDto<FollowListResponseDto> getFollowList(Long targetMemberId, String type, int page, int size);
}
