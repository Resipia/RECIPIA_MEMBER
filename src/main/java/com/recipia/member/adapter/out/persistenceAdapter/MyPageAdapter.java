package com.recipia.member.adapter.out.persistenceAdapter;

import com.recipia.member.adapter.in.web.dto.response.FollowingListResponseDto;
import com.recipia.member.adapter.out.persistenceAdapter.querydsl.MyPageQueryRepository;
import com.recipia.member.application.port.out.port.MyPagePort;
import com.recipia.member.domain.MyPage;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class MyPageAdapter implements MyPagePort {

    private final MyPageQueryRepository myPageQueryRepository;

    /**
     * [READ] 마이페이지 조회
     * 조회에 성공하면 회원 정보를 반환한다.
     */
    @Override
    public MyPage viewMyPage(Long memberId) {
        return myPageQueryRepository.viewMyPage(memberId);
    }

    /**
     * [UPDATE] 마이페이지 수정
     * 마이페이지에서 기본 정보를 수정한 후 업데이트 된 row 갯수를 반환한다.
     */
    @Override
    public Long updateMyPage(MyPage requestMyPage) {
        return myPageQueryRepository.updateMyPage(requestMyPage);
    }

    /**
     * [READ] 팔로잉 목록 가져오기
     */
    @Override
    public Page<FollowingListResponseDto> getFollowingList(Long targetMemberId, Long loggedMemberId, Pageable pageable) {
        return myPageQueryRepository.getFollowingList(targetMemberId, loggedMemberId, pageable);
    }
}
