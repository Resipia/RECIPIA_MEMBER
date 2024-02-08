package com.recipia.member.adapter.in.listener.aws;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.recipia.member.adapter.in.listener.aws.dto.MessageMemberIdDto;
import com.recipia.member.adapter.in.listener.aws.dto.SnsNotificationDto;
import com.recipia.member.application.port.in.MemberEventRecordUseCase;
import com.recipia.member.common.utils.MemberStringUtils;
import io.awspring.cloud.sqs.annotation.SqsListener;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 이벤트 저장소 업데이트 담당
 * MEMBER 서버에서 SNS가 잘 발행되었을때 DB MEMBER_EVENT_RECORD테이블에 true로 값 업데이트 해주는 SQS 리스너
 */
@Slf4j
@Transactional
@RequiredArgsConstructor
@Service
public class SqsEventRecordListener {

    private final ObjectMapper objectMapper;
    private final MemberEventRecordUseCase memberEventRecordUseCase;

    @SqsListener(value = "${spring.cloud.aws.sqs.record-sqs-name}")
    public void receiveSnsPublishedMessage(String messageJson) throws JsonProcessingException {

        SnsNotificationDto snsNotificationDto = objectMapper.readValue(messageJson, SnsNotificationDto.class);

        String topicName = MemberStringUtils.extractLastPart(snsNotificationDto.TopicArn());

        // SNS 받으면 동일한 메세지와 토픽으로 발행된 이벤트는 이벤트 저장소에서 published=true로 바꿔주기
        memberEventRecordUseCase.changePublishedToTrue(snsNotificationDto.Message(), topicName);

    }
}
