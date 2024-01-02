package com.recipia.member.application.service;

import com.recipia.member.adapter.out.aws.TokyoSnsService;
import com.recipia.member.application.port.in.AuthUseCase;
import com.recipia.member.common.event.SendVerifyCodeSpringEvent;
import com.recipia.member.domain.Authentication;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.Optional;
import java.util.Random;

@RequiredArgsConstructor
@Service
public class AuthService implements AuthUseCase {

    private final TokyoSnsService tokyoSnsService;
    private final ApplicationEventPublisher eventPublisher;
    private final RedisService redisService;

    @Override
    public void verifyPhoneNumber(Authentication authentication) {
        authentication.formatPhoneNumber();
        String verificationCode = generateRandomCode();

        // aws SMS 이용해서 문자 전송
        tokyoSnsService.sendVerificationCode(authentication.getPhoneNumber(), verificationCode);

        // 문자 메시지 발생 완료 스프링 이벤트 발행
        eventPublisher.publishEvent(new SendVerifyCodeSpringEvent(authentication.getPhoneNumber(), verificationCode));

    }

    // 랜덤 6자리 숫자 생성
    private String generateRandomCode() {
        Random random = new Random();
        int number = random.nextInt(900000) + 100000; // 100000부터 999999까지
        return String.valueOf(number);
    }

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
}
