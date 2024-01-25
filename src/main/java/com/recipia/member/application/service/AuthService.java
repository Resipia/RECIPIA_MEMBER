package com.recipia.member.application.service;

import com.recipia.member.adapter.out.aws.TokyoSnsService;
import com.recipia.member.application.port.in.AuthUseCase;
import com.recipia.member.application.port.out.port.*;
import com.recipia.member.common.event.SendVerifyCodeSpringEvent;
import com.recipia.member.common.exception.ErrorCode;
import com.recipia.member.common.exception.MemberApplicationException;
import com.recipia.member.domain.Authentication;
import com.recipia.member.domain.Logout;
import com.recipia.member.domain.converter.JwtConverter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.Random;

/**
 * 본인 인증 서비스 클래스
 */
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class AuthService implements AuthUseCase {

    private final TokyoSnsService tokyoSnsService;
    private final ApplicationEventPublisher eventPublisher;
    private final RedisService redisService;
    private final JwtPort jwtPort;
    private final JwtConverter jwtConverter;
    private final MemberPort memberPort;
    private final MemberManagementPort memberManagementPort;
    private final FollowPort followPort;

    /**
     * [READ] 전화번호로 인증 코드 전송 메서드 호출
     */
    @Override
    public void verifyPhoneNumber(Authentication authentication) {

        // db에 이미 존재하는 전화번호인지 확인
        boolean isAvailableTelNo = memberManagementPort.isTelNoAvailable(authentication.getPhoneNumber());

        if (!isAvailableTelNo) {
            throw new MemberApplicationException(ErrorCode.TEL_NO_ALREADY_EXISTS);
        }

        // 휴대폰 번호에 국제번호 추가
        authentication.formatPhoneNumber();
        String verificationCode = generateRandomCode();

        // aws SMS 이용해서 문자 전송
        tokyoSnsService.sendVerificationCode(authentication.getPhoneNumber(), verificationCode);

        // 문자 메시지 발생 완료 스프링 이벤트 발행
        eventPublisher.publishEvent(new SendVerifyCodeSpringEvent(authentication.getPhoneNumber(), verificationCode));

    }


    /**
     * [DELETE/CREATE] 로그아웃
     * 1. 리프레시 토큰을 DB에서 제거
     * 2. token blacklist에 access token 저장
     */
    @Transactional
    @Override
    public void logout(Logout logout) {
        // 1. DB에서 리프레시 토큰 삭제
        jwtPort.deleteRefreshToken(logout.getMemberId());

        // 2. token blacklist에 추가
        jwtPort.insertTokenBlacklist(jwtConverter.logoutToTokenBlacklist(logout));
    }

    /**
     * 인증코드 검증
     * 인증코드가 일치하면 true, 틀리면 false 반환한다.
     */
    @Override
    public boolean checkVerifyCode(Authentication authentication) {
        authentication.formatPhoneNumber();

        String phoneNumber = authentication.getPhoneNumber();

        return Optional.ofNullable(redisService.getValues(phoneNumber))
                .filter(verifyCode -> verifyCode.equals(authentication.getVerifyCode()))
                .map(verifyCode -> {
                    redisService.deleteValues(phoneNumber); // 인증 코드가 일치하면 레디스에서 해당 키값을 삭제한다.
                    return true;
                })
                .orElse(false);
    }


    /**
     * [UPDATE] 회원 탈퇴
     * 수정된 row 갯수를 반환한다.
     */
    @Transactional
    @Override
    public Long deactivateMember(Long memberId) {
        Long updatedCount = memberPort.deactivateMember(memberId);

        // 프로필 사진을 삭제한다.
        memberPort.softDeleteProfileImageByMemberId(memberId);

        // 팔로잉/팔로우 관계를 삭제한다.
        followPort.deleteFollowsByMemberId(memberId);

        // todo: 회원 동의는 나중에 탈퇴 경과 1년 후 삭제한다.

        return updatedCount;
    }


    /**
     * 랜덤 6자리 숫자 생성
     */
    private String generateRandomCode() {
        Random random = new Random();
        int number = random.nextInt(900000) + 100000; // 100000부터 999999까지
        return String.valueOf(number);
    }


}
