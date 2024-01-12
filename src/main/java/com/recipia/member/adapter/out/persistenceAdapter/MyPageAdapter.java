package com.recipia.member.adapter.out.persistenceAdapter;

import com.recipia.member.adapter.out.persistenceAdapter.querydsl.MyPageQueryRepository;
import com.recipia.member.application.port.out.port.MyPagePort;
import com.recipia.member.domain.MyPage;
import lombok.RequiredArgsConstructor;
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
        MyPage myPage = myPageQueryRepository.viewMyPage(memberId);
        return myPage;
    }

    @Override
    public Long updateMyPage(MyPage requestMyPage) {
        Long updatedCount = myPageQueryRepository.updateMyPage(requestMyPage);
        return updatedCount;
    }
}
