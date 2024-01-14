package com.recipia.member.application.service;

import com.recipia.member.application.port.in.SignUpUseCase;
import com.recipia.member.application.port.out.port.MemberPort;
import com.recipia.member.application.port.out.port.SignUpPort;
import com.recipia.member.common.event.SignUpSpringEvent;
import com.recipia.member.common.exception.ErrorCode;
import com.recipia.member.common.exception.MemberApplicationException;
import com.recipia.member.domain.Member;
import com.recipia.member.domain.MemberFile;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@RequiredArgsConstructor
@Service
public class SignUpService implements SignUpUseCase {

    private final SignUpPort signUpPort;
    private final ImageS3Service imageS3Service;
    private final ApplicationEventPublisher eventPublisher;
    private final MemberPort memberPort;

    @Transactional
    @Override
    public Long signUp(Member member, MultipartFile profileImage) {
        // 비밀번호 형태 검증
        if (!member.isValidPassword(member.getPassword())) {
            throw new MemberApplicationException(ErrorCode.BAD_REQUEST);
        }

        // 비밀번호 암호화
        member.passwordEncoder();

        Long savedMemberId = signUpPort.signUpMember(member);

        // 파일이 null이면 저장하지 않는다.
        if(profileImage != null && !profileImage.isEmpty()) {
            // 프로필 파일 저장을 위한 엔티티 생성 (이때 s3에는 이미 이미지가 업로드 완료되고 저장된 경로의 url을 받은 엔티티를 리스트로 생성)
            MemberFile memberFile = imageS3Service.createMemberFile(profileImage, 0, savedMemberId);
            Long savedMemberFileId = memberPort.saveMemberFile(memberFile);

            if(savedMemberFileId < 0) {
                throw new MemberApplicationException(ErrorCode.MEMBER_FILE_SAVE_ERROR);
            }
        }

        // 이벤트 발행: 레시피 서버에 회원가입된 유저 정보 저장
        eventPublisher.publishEvent(SignUpSpringEvent.of(savedMemberId));

        return savedMemberId;
    }

}
