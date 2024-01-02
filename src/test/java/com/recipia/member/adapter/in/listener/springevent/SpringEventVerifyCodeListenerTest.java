package com.recipia.member.adapter.in.listener.springevent;

import com.recipia.member.application.service.RedisService;
import com.recipia.member.common.event.SendVerifyCodeSpringEvent;
import com.recipia.member.config.TotalTestSupport;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.context.ApplicationContext;

import java.time.Duration;

@DisplayName("[통합] SendVerifyCode 스프링 이벤트 리스너 테스트")
class SpringEventVerifyCodeListenerTest extends TotalTestSupport {

    @Autowired
    private ApplicationContext applicationContext;

    @SpyBean
    private SpringEventVerifyCodeListener springEventVerifyCodeListener;

    @MockBean
    private RedisService redisService;

    @Test
    @DisplayName("[happy] 문자 메시지 전송 이벤트 발행시 리스너가 반응하여 동작하는지를 검증한다.")
    void whenEventPublished_thenEventListenerIsTriggered() {
        // given
        SendVerifyCodeSpringEvent event = createSendVerifyCondeSpringEvent();

        // when
        applicationContext.publishEvent(event);

        // then
        Mockito.verify(springEventVerifyCodeListener).eventVerifyCodeListener(event);
    }

    @DisplayName("[happy] 문자 메시지 전송 이벤트 발행시 리스너 메서드가 호출되고 그 내부의 레디스에 전화번호와 인증코드를 저장하는 로직이 호출된다.")
    @Test
    void whenEventPublished_thenTriggerEventListenerMethods() {
        //given
        SendVerifyCodeSpringEvent event = createSendVerifyCondeSpringEvent();
        Duration TIMEOUT = Duration.ofMinutes(5); // 5분

        //when
        applicationContext.publishEvent(event);

        //then
        Mockito.verify(redisService).setValues(event.phoneNumber(), event.verificationCode(), TIMEOUT);

    }

    private SendVerifyCodeSpringEvent createSendVerifyCondeSpringEvent() {
        String testPhoneNumber = "01012345678";
        String testVerificationCode = "123456";
        return SendVerifyCodeSpringEvent.of(testPhoneNumber, testVerificationCode);
    }

}