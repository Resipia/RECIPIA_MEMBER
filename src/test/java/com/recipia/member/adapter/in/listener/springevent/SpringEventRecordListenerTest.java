package com.recipia.member.adapter.in.listener.springevent;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.recipia.member.application.port.in.MemberEventRecordUseCase;
import com.recipia.member.common.event.NicknameChangeSpringEvent;
import com.recipia.member.config.TotalTestSupport;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.context.ApplicationContext;

import static org.mockito.Mockito.verify;

@DisplayName("[통합] NicknameChange 스프링 이벤트 리스너 테스트")
class SpringEventRecordListenerTest extends TotalTestSupport {

    @Autowired
    private ApplicationContext applicationContext;

    @SpyBean
    private SpringEventRecordListener springEventRecordListener;

    @MockBean
    private MemberEventRecordUseCase memberEventRecordUseCase;

    @DisplayName("[happy] 닉네임 변경 이벤트 발행시 리스너가 반응하여 동작하는지 검증")
    @Test
    void whenEventPublished_thenEventListenerIsTriggered() throws JsonProcessingException {
        // given
        NicknameChangeSpringEvent event = NicknameChangeSpringEvent.of(1L);
        // when
        applicationContext.publishEvent(event);
        // then
        verify(springEventRecordListener).nicknameChangeEventRecordListener(event);
    }

}