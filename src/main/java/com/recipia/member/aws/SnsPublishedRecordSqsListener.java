package com.recipia.member.aws;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.recipia.member.domain.event.MemberEventRecord;
import com.recipia.member.exception.ErrorCode;
import com.recipia.member.exception.MemberApplicationException;
import com.recipia.member.repository.MemberEventRecordRepository;
import com.recipia.member.utils.MemberStringUtils;
import io.awspring.cloud.sqs.annotation.SqsListener;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Transactional
@RequiredArgsConstructor
@Service
public class SnsPublishedRecordSqsListener {

    private final ObjectMapper objectMapper;
    private final MemberEventRecordRepository memberEventRecordRepository;


    /**
     * MEMBER 서버에서 SNS가 잘 발행되었을때 DB MEMBER_EVENT_RECORD테이블에 true로 값 업데이트 해주는 SQS
     */
    @SqsListener(value = "${spring.cloud.aws.sqs.record-sqs-name}")
    public void receiveSnsPublishedMessage(String messageJson) throws JsonProcessingException {
        JsonNode rootNode = objectMapper.readTree(messageJson);
        String messageId = rootNode.get("MessageId").asText();
        String topicArn = rootNode.get("TopicArn").asText();
        String topicName = MemberStringUtils.extractLastPart(topicArn);

        // 'Message' 필드 내의 JSON 문자열 추출 및 파싱
        String innerMessageJson = rootNode.get("Message").asText();
        JsonNode innerMessageNode = objectMapper.readTree(innerMessageJson);

        // 'message' 필드 내의 JSON 문자열 추출 및 파싱
        String memberMessageJson = innerMessageNode.get("message").asText();
        JsonNode memberMessageNode = objectMapper.readTree(memberMessageJson);

        // 'memberId' 추출
        Long memberId = Long.valueOf(memberMessageNode.get("memberId").asText());

        // DB 업데이트 로직
        MemberEventRecord memberEventRecord = memberEventRecordRepository
                .findFirstByMember_IdAndSnsTopicOrderByIdDesc(memberId, topicName)
                .orElseThrow(() -> new MemberApplicationException(ErrorCode.DB_ERROR));

        memberEventRecord.changePublished();
        log.info("[MEMBER] Processed message for memberId: {} with messageId: {}", memberId, messageId);
    }


}
