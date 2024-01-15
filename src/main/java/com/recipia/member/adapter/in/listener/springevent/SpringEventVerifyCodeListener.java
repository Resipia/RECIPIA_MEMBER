package com.recipia.member.adapter.in.listener.springevent;

import com.recipia.member.application.service.RedisService;
import com.recipia.member.common.event.SendVerifyCodeSpringEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.time.Duration;

/**
 * 인증 코드 관련 스프링 이벤트 리스너
 */
@Slf4j
@RequiredArgsConstructor
@Component
public class SpringEventVerifyCodeListener {

    private final RedisService redisService;
    private final Duration TIMEOUT = Duration.ofMinutes(5); // 5분

    /**
     * Redis에 휴대폰 번호와 인증코드 저장
     */
    @EventListener
    public void eventVerifyCodeListener(SendVerifyCodeSpringEvent event) {
        String phoneNumber = event.phoneNumber();
        String verificationCode = event.verificationCode();

        // Redis에 저장 (키: 전화번호, 값: 인증코드, 만료 시간: 5분)
        redisService.setValues(phoneNumber, verificationCode, TIMEOUT);
        log.info("Saved in Redis with expiration: key {}, value {}, duration {}", phoneNumber, verificationCode, TIMEOUT);
    }

}
