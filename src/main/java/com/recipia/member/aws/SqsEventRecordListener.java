package com.recipia.member.aws;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.recipia.member.domain.event.MemberEventRecord;
import com.recipia.member.dto.message.MessageMemberIdDto;
import com.recipia.member.dto.message.SnsNotificationDto;
import com.recipia.member.exception.ErrorCode;
import com.recipia.member.exception.MemberApplicationException;
import com.recipia.member.repository.MemberEventRecordRepository;
import com.recipia.member.utils.MemberStringUtils;
import io.awspring.cloud.sqs.annotation.SqsListener;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * MEMBER 서버에서 SNS가 잘 발행되었을때 DB MEMBER_EVENT_RECORD테이블에 true로 값 업데이트 해주는 SQS
 * 단, Message 안에 traceId가 있을때만 정상 실행 되도록 구성
 * traceId 없을때는 에러로그만 남기기 (왜냐하면, 어차피 배치 작업을 통해 SNS 재발행 프로세스가 진행되기 때문.)
 */
@Slf4j
@Transactional
@RequiredArgsConstructor
@Service
public class SqsEventRecordListener {

    private final ObjectMapper objectMapper;
    private final MemberEventRecordRepository memberEventRecordRepository;

    @SqsListener(value = "${spring.cloud.aws.sqs.record-sqs-name}")
    public void receiveSnsPublishedMessage(String messageJson) throws JsonProcessingException {

        SnsNotificationDto snsNotificationDto = objectMapper.readValue(messageJson, SnsNotificationDto.class);
        MessageMemberIdDto messageMemberIdDto = objectMapper.readValue(snsNotificationDto.Message(), MessageMemberIdDto.class);

        String topicName = MemberStringUtils.extractLastPart(snsNotificationDto.TopicArn());
        Long memberId = messageMemberIdDto.memberId();

        MemberEventRecord memberEventRecord = memberEventRecordRepository
                .findFirstByMember_IdAndSnsTopicOrderByIdDesc(memberId, topicName)
                .orElseThrow(() -> new MemberApplicationException(ErrorCode.EVENT_NOT_FOUND));

        memberEventRecord.changePublished();
    }
}
