package com.recipia.member.application.service;

import com.recipia.member.adapter.out.persistence.constant.MemberStatus;
import com.recipia.member.application.port.in.MyPageUseCase;
import com.recipia.member.application.port.out.port.MemberPort;
import com.recipia.member.application.port.out.port.MyPagePort;
import com.recipia.member.common.event.NicknameChangeSpringEvent;
import com.recipia.member.common.exception.ErrorCode;
import com.recipia.member.common.exception.MemberApplicationException;
import com.recipia.member.domain.Member;
import com.recipia.member.domain.MyPage;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

@RequiredArgsConstructor
@Service
public class MyPageService implements MyPageUseCase {

    private final MyPagePort myPagePort;
    private final ApplicationEventPublisher eventPublisher;
    private final MemberPort memberPort;

    @Override
    public MyPage viewMyPage(MyPage myPage) {
        myPage = myPagePort.viewMyPage(myPage.getMemberId());
        return myPage;
    }

    /**
     * 마이페이지 수정, 수정에 성공하면 수정 완료된 마이페이지 데이터를 반환한다.
     */
    @Transactional
    @Override
    public MyPage updateAndViewMyPage(MyPage myPage) {
        // 수정된 데이터 갯수
        final Long EXPECTED_UPDATE_COUNT = 1L;

        // 업데이트 하기 전 DB에 있는 멤버 도메인
        Member beforeMember = memberPort.findMemberByIdAndStatus(myPage.getMemberId(), MemberStatus.ACTIVE);
        boolean isNicknameChanged = !Objects.equals(beforeMember.getNickname(), myPage.getNickname());

        Long updatedCount = myPagePort.updateMyPage(myPage);
        boolean isUpdateSuccessful = Objects.equals(EXPECTED_UPDATE_COUNT, updatedCount);

        // 수정된 데이터 갯수가 한개라면 업데이트된 마이페이지 정보 반환
        if (isUpdateSuccessful) {
            // 닉네임이 수정되었으면 외부 서버로 SNS 발행하기 위한 스프링 이벤트 발행
            if (isNicknameChanged) {
                eventPublisher.publishEvent(new NicknameChangeSpringEvent(myPage.getMemberId()));
            }
            return viewMyPage(myPage);
        } else {
            throw new MemberApplicationException(ErrorCode.DB_ERROR);
        }
    }
}
