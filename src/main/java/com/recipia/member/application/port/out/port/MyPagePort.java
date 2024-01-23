package com.recipia.member.application.port.out.port;

import com.recipia.member.adapter.in.web.dto.response.FollowListResponseDto;
import com.recipia.member.domain.MyPage;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface MyPagePort {
    MyPage viewMyPage(Long memberId, Long targetMemberId);
    Long updateMyPage(MyPage requestMyPage);

    Page<FollowListResponseDto> getFollowList(Long targetMemberId, Long loggedMemberId, String type, Pageable pageable);
}
