package com.recipia.member.application.service;

import com.recipia.member.adapter.out.aws.TokyoSnsService;
import com.recipia.member.application.port.in.AuthUseCase;
import com.recipia.member.application.port.out.port.JwtPort;
import com.recipia.member.common.event.SendVerifyCodeSpringEvent;
import com.recipia.member.common.exception.ErrorCode;
import com.recipia.member.common.exception.MemberApplicationException;
import com.recipia.member.domain.Authentication;
import com.recipia.member.domain.Logout;
import com.recipia.member.domain.TokenBlacklist;
import com.recipia.member.domain.converter.JwtConverter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Random;

@RequiredArgsConstructor
@Service
public class AuthService implements AuthUseCase {

    private final TokyoSnsService tokyoSnsService;
    private final ApplicationEventPublisher eventPublisher;
    private final RedisService redisService;
    private final MemberManagementService memberManagementService;
    private final JwtPort jwtPort;
    private final JwtConverter jwtConverter;

    @Override
    public void verifyPhoneNumber(Authentication authentication) {

        // db에 이미 존재하는 전화번호인지 확인
        boolean isAvailableTelNo = memberManagementService.isTelNoAvailable(authentication.getPhoneNumber());

        if (!isAvailableTelNo) {
            throw new MemberApplicationException(ErrorCode.TEL_NO_ALREADY_EXISTS);
        }

        authentication.formatPhoneNumber();
        String verificationCode = generateRandomCode();

        // aws SMS 이용해서 문자 전송
        tokyoSnsService.sendVerificationCode(authentication.getPhoneNumber(), verificationCode);

        // 문자 메시지 발생 완료 스프링 이벤트 발행
        eventPublisher.publishEvent(new SendVerifyCodeSpringEvent(authentication.getPhoneNumber(), verificationCode));

    }


    @Override
    public void logout(Logout logout) {
        // DB에서 리프레시 토큰 삭제
        jwtPort.deleteRefreshToken(logout.getMemberId());

        // token blacklist에 추가
        jwtPort.insertTokenBlacklist(jwtConverter.logoutToTokenBlacklist(logout));
    }

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


    // 랜덤 6자리 숫자 생성
    private String generateRandomCode() {
        Random random = new Random();
        int number = random.nextInt(900000) + 100000; // 100000부터 999999까지
        return String.valueOf(number);
    }


}
