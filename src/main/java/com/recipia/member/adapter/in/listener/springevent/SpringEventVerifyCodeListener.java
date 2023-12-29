package com.recipia.member.adapter.in.listener.springevent;

import com.recipia.member.application.service.RedisService;
import com.recipia.member.common.event.SendVerifyCodeSpringEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Slf4j
@RequiredArgsConstructor
@Component
public class SpringEventVerifyCodeListener {

    private final RedisService redisService;
    private static final int TIMEOUT = 5 * 60; // 5분을 초로 변환

    @EventListener
    public void eventVerifyCodeListener(SendVerifyCodeSpringEvent event) {
        String phoneNumber = event.phoneNumber();
        String verificationCode = event.verificationCode();

        // Redis에 저장 (키: 전화번호, 값: 인증코드)
        redisService.setValues(phoneNumber, verificationCode);
        log.info("Saved Redis: key {}, value {}", phoneNumber,verificationCode);

        // 설정한 만료 시간 (5분)
        redisService.expireValues(phoneNumber, TIMEOUT);
    }

}
