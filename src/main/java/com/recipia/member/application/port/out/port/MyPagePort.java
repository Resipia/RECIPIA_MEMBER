package com.recipia.member.application.port.out.port;

import com.recipia.member.adapter.in.web.dto.response.FollowingListResponseDto;
import com.recipia.member.domain.MyPage;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface MyPagePort {
    MyPage viewMyPage(Long memberId);
    Long updateMyPage(MyPage requestMyPage);

    Page<FollowingListResponseDto> getFollowingList(Long targetMemberId, Long loggedMemberId, Pageable pageable);
}
