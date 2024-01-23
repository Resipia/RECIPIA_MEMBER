package com.recipia.member.application.service;

import com.recipia.member.adapter.in.web.dto.response.FollowListResponseDto;
import com.recipia.member.adapter.in.web.dto.response.PagingResponseDto;
import com.recipia.member.adapter.out.persistence.constant.MemberStatus;
import com.recipia.member.application.port.in.MyPageUseCase;
import com.recipia.member.application.port.out.port.MemberPort;
import com.recipia.member.application.port.out.port.MyPagePort;
import com.recipia.member.common.event.NicknameChangeSpringEvent;
import com.recipia.member.common.exception.ErrorCode;
import com.recipia.member.common.exception.MemberApplicationException;
import com.recipia.member.common.utils.SecurityUtils;
import com.recipia.member.domain.Member;
import com.recipia.member.domain.MemberFile;
import com.recipia.member.domain.MyPage;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * 마이페이지 서비스 클래스
 */
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class MyPageService implements MyPageUseCase {

    private final MyPagePort myPagePort;
    private final ApplicationEventPublisher eventPublisher;
    private final MemberPort memberPort;
    private final ImageS3Service imageS3Service;
    private final SecurityUtils securityUtils;

    /**
     * [READ] 마이페이지 조회를 담당하는 메서드
     * 1. db에 저장된 멤버 정보를 가져온다.
     * 2. 프로필 이미지 저장 경로를 기반으로 프로필 이미지의 pre-signed-url을 생성해서 profileIamgeUrl에 세팅한다.
     * 위 단계가 끝나면 마이 페이지 데이터를 반환한다.
     */
    @Override
    public MyPage viewMyPage(Long memberId) {
        // 1. 멤버의 기본정보 가져오기
        MyPage myPage = myPagePort.viewMyPage(memberId);

        // 2. 프로필 이미지 경로 확인 및 처리
        String filePath = myPage.getImageFilePath();
        if (filePath != null && !filePath.isEmpty()) {
            String preSignedUrl = imageS3Service.generatePreSignedUrl(filePath, 60);
            myPage.setImagePreUrl(preSignedUrl);
        }
        return myPage;
    }

    /**
     * [UPDATE] 마이페이지 수정을 담당하는 메서드
     * 1. 수정 요청한 멤버가 DB에 존재하는 멤버인지 검증한다.
     * 2. 닉네임이 수정됐는지 검증한다.
     * 3. 멤버의 기본정보를 수정한다.
     * 4. 삭제된 프로필 이미지 파일이 있다면(deleteFileOrder != null) soft delete를 진행한다.
     * 5. 프로필 이미지가 새로 들어왔다면 저장한다.
     * 6. 만약 닉네임이 수정되었다면 '닉네임 변경' 스프링 이벤트 발행
     */
    @Transactional
    @Override
    public Long updateMyPage(MyPage myPage, MultipartFile profileImage) {
        Long memberId = myPage.getMemberId();

        // 1. DB에 존재하고 활성화 상태인 멤버인지 검증
        Member beforeMember = memberPort.findMemberByIdAndStatus(myPage.getMemberId(), MemberStatus.ACTIVE);

        // 2. 닉네임도 수정되었는지 검증한다.
        boolean isNicknameChanged = !Objects.equals(beforeMember.getNickname(), myPage.getNickname());

        // 3. 기본 정보를 수정한다.
        Long updatedCount = myPagePort.updateMyPage(myPage);

        // 4. 삭제된 프로필 이미지 파일이 있다면 soft delete 처리한다.
        if(myPage.getDeleteFileOrder() != null) {
            memberPort.softDeleteProfileImage(myPage);
        }

        // 5. 수정된 profileImage가 있다면 저장한다.
        if (profileImage != null && !profileImage.isEmpty()) {
            // 프로필 파일 저장을 위한 엔티티 생성 (이때 s3에는 이미 이미지가 업로드 완료되고 저장된 경로의 url을 받은 엔티티를 리스트로 생성)
            MemberFile memberFile = imageS3Service.createMemberFile(profileImage, memberId);
            Long savedMemberFileId = memberPort.saveMemberFile(memberFile);

            if(savedMemberFileId < 1) {
                throw new MemberApplicationException(ErrorCode.MEMBER_FILE_SAVE_ERROR);
            }
        }

        // 6. 닉네임이 수정되었으면 외부 서버로 SNS 발행하기 위한 스프링 이벤트 발행
        if (isNicknameChanged) {
            eventPublisher.publishEvent(new NicknameChangeSpringEvent(myPage.getMemberId()));
        }

        return updatedCount;
    }

    /**
     * [READ] targetMemberId가 팔로우하거나 targetMemberId를 팔로잉하는 회원의 목록 가져오기
     * 페이징을 위한 Pageable 객체를 여기서 조립해서 사용한다.
     */
    @Override
    public PagingResponseDto<FollowListResponseDto> getFollowList(Long targetMemberId, String type, int page, int size) {
        // 1. 정렬조건을 정한 뒤 Pageable 객체 생성
        Pageable pageable = PageRequest.of(page, size);

        // 2. 본인 확인용 memberId 추출
        Long memberId = securityUtils.getCurrentMemberId();

        // 3. 데이터를 받아온다.
        Page<FollowListResponseDto> followingList = myPagePort.getFollowList(targetMemberId, memberId, type, pageable);

        // 4. 받아온 데이터를 꺼내서 응답 dto에 값을 세팅해준다.
        List<FollowListResponseDto> content = followingList.getContent();

        // 5. 이미지 경로를 이용해서 pre-signed-url을 생성하고 세팅해준 값으로 새로운 List를 만든다.
        List<FollowListResponseDto> updatedContent = content.stream().map(following -> {
            String preUrl = Optional.ofNullable(following.getImageFullPath())
                    .filter(path -> !path.isEmpty())
                    .map(path -> imageS3Service.generatePreSignedUrl(path, 60))
                    .orElse(null);
            return FollowListResponseDto.of(following.getMemberId(), preUrl, following.getNickname(), following.getFollowId(), following.isMe());
        }).toList();

        Long totalCount = followingList.getTotalElements();
        return PagingResponseDto.of(updatedContent, totalCount);
    }

}
