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
     * MEMBER 서버에서 SNS가 잘 발행되었을때 DB MEMBER_EVENT_RECORD테이블에 false로 값 업데이트 해주는 SQS
     */
    @SqsListener(value = "${spring.cloud.aws.sqs.record-sqs-name}")
    public void receiveSnsPublishedMessage(String messageJson) throws JsonProcessingException {

        JsonNode messageNode = objectMapper.readTree(messageJson);
        String messageId = messageNode.get("MessageId").asText();  // 메시지 ID 추출

        // SQS 메시지 처리 로직
        String topicArn = messageNode.get("TopicArn").asText();
        String topicName = MemberStringUtils.extractLastPart(topicArn);

        String messageContent = messageNode.get("Message").asText();

        log.info("[MEMBER] Received message from SQS with messageId: {}", messageId);

        // Assuming the "Message" is also a JSON string, we parse it to print as JSON object
        JsonNode message = objectMapper.readTree(messageContent);
        log.info("Message:  {}", message.toString());

        ObjectNode node = (ObjectNode) objectMapper.readTree(message.toString());

        // "memberId" 키에 해당하는 값을 추출
        Long memberId = Long.valueOf(node.get("memberId").asText());

        MemberEventRecord memberEventRecord = memberEventRecordRepository.findFirstByMember_IdAndSnsTopicOrderByIdDesc(memberId, topicName).orElseThrow(() -> new MemberApplicationException(ErrorCode.DB_ERROR));

        memberEventRecord.changePublished();

    }


}
