package com.recipia.member.adapter.out.persistenceAdapter;

import com.recipia.member.adapter.in.web.dto.response.FollowListResponseDto;
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
    public MyPage viewMyPage(Long memberId, Long targetMemberId) {

        // 조회하려는 마이페이지가 다른 회원의 마이페이지라면 팔로우 id까지 조회
        if (!memberId.equals(targetMemberId)) {
            return myPageQueryRepository.viewOtherMyPage(memberId, targetMemberId);
        }
        // 조회하려는 마이페이지가 로그인한 계정의 마이페이지라면 팔로우 id 조회는 제거
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
     * [READ] 팔로잉/팔로워 목록 가져오기
     */
    @Override
    public Page<FollowListResponseDto> getFollowList(Long targetMemberId, Long loggedMemberId, String type, Pageable pageable) {
        return myPageQueryRepository.getFollowingList(targetMemberId, loggedMemberId, type, pageable);
    }

}
