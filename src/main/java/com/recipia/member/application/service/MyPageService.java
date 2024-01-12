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

/**
 * 마이페이지 서비스 클래스
 */
@RequiredArgsConstructor
@Service
public class MyPageService implements MyPageUseCase {

    private final MyPagePort myPagePort;
    private final ApplicationEventPublisher eventPublisher;
    private final MemberPort memberPort;
    private final ImageS3Service imageS3Service;


    /**
     * [READ] 마이페이지 조회를 담당하는 메서드
     * 1단계 - db에 저장된 멤버 정보를 가져온다.
     * 2단계 - 프로필 이미지 저장 경로를 기반으로 프로필 이미지의 pre-signed-url을 생성해서 profileIamgeUrl에 세팅한다.
     * 위 단계가 끝나면 마이 페이지 데이터를 반환한다.
     */
    @Override
    public MyPage viewMyPage(Long memberId) {
        // 1단계
        MyPage myPage = myPagePort.viewMyPage(memberId);

        // 2단계
        String preSignedUrl = imageS3Service.generatePreSignedUrl(myPage.getProfileImageFilePath(), 60); // 여기서 'profileImageUrl'는 MyPage 객체에 저장되어 있는 파일 경로를 의미한다.
        myPage.setProfileImageUrl(preSignedUrl);

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
            return viewMyPage(myPage.getMemberId());
        } else {
            throw new MemberApplicationException(ErrorCode.DB_ERROR);
        }
    }
}
