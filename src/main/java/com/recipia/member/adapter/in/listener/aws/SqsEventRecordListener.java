package com.recipia.member.adapter.in.listener.aws;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
<<<<<<< Updated upstream:src/main/java/com/recipia/member/hexagonal/adapter/in/listener/aws/SqsEventRecordListener.java
import com.recipia.member.hexagonal.adapter.out.persistence.MemberEventRecordEntity;
import com.recipia.member.hexagonal.adapter.in.listener.aws.dto.MessageMemberIdDto;
import com.recipia.member.hexagonal.adapter.in.listener.aws.dto.SnsNotificationDto;
import com.recipia.member.hexagonal.application.port.in.MemberEventRecordUseCase;
import com.recipia.member.hexagonal.application.port.out.port.MemberEventRecordPort;
import com.recipia.member.hexagonal.common.exception.ErrorCode;
import com.recipia.member.hexagonal.common.exception.MemberApplicationException;
import com.recipia.member.hexagonal.adapter.out.persistenceAdapter.MemberEventRecordRepository;
import com.recipia.member.hexagonal.common.utils.MemberStringUtils;
=======
import com.recipia.member.adapter.out.persistenceAdapter.MemberEventRecordRepository;
import com.recipia.member.adapter.out.persistence.MemberEventRecordEntity;
import com.recipia.member.adapter.in.listener.aws.dto.MessageMemberIdDto;
import com.recipia.member.adapter.in.listener.aws.dto.SnsNotificationDto;
import com.recipia.member.common.exception.ErrorCode;
import com.recipia.member.common.exception.MemberApplicationException;
import com.recipia.member.common.utils.MemberStringUtils;
>>>>>>> Stashed changes:src/main/java/com/recipia/member/adapter/in/listener/aws/SqsEventRecordListener.java
import io.awspring.cloud.sqs.annotation.SqsListener;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * MEMBER 서버에서 SNS가 잘 발행되었을때 DB MEMBER_EVENT_RECORD테이블에 true로 값 업데이트 해주는 SQS
 * 단, Message 안에 traceId가 있을때만 정상 실행 되도록 구성
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
        MessageMemberIdDto messageMemberIdDto = objectMapper.readValue(snsNotificationDto.Message(), MessageMemberIdDto.class);

        String topicName = MemberStringUtils.extractLastPart(snsNotificationDto.TopicArn());
        Long memberId = messageMemberIdDto.memberId();

        memberEventRecordUseCase.changePublishedToTrue(memberId, topicName);

    }
}
