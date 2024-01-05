package com.recipia.member.application.service;

import com.recipia.member.application.port.out.port.MemberEventRecordPort;
import com.recipia.member.common.utils.CustomJsonBuilder;
import com.recipia.member.config.aws.SnsConfig;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("[단위] 멤버 이벤트 저장소 서비스 테스트")
class MemberEventRecordServiceTest {

    @Mock
    private MemberEventRecordPort memberEventRecordPort;

    @Mock
    private SnsConfig snsConfig;

    @Mock
    private CustomJsonBuilder customJsonBuilder;

    @InjectMocks
    private MemberEventRecordService memberEventRecordService;

    @DisplayName("[happy] memberId, topicName에 해당하는 가장 최근 이벤트 발행 처리(published = true) 업데이트 성공")
    @Test
    void testChangePublishedToTrue() {
        // given
        Long memberId = 1L;
        String topicName = "topicName";
        when(memberEventRecordPort.changePublishedToTrue(memberId, topicName)).thenReturn(1L);

        // when
        memberEventRecordService.changePublishedToTrue(memberId, topicName);

        // then
        verify(memberEventRecordPort).changePublishedToTrue(memberId, topicName);
    }



}