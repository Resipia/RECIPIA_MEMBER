package com.recipia.member.aws;

import brave.Span;
import brave.Tracer;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.awspring.cloud.sqs.annotation.SqsListener;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class SqsListenerService {

    private final ObjectMapper objectMapper;
    private final Tracer tracer;

    @SqsListener(value = "${spring.cloud.aws.sqs.nickname-sqs-name}")
    public void receiveMessage(String messageJson) throws JsonProcessingException {

        JsonNode messageNode = objectMapper.readTree(messageJson);
        String messageId = messageNode.get("MessageId").asText();  // 메시지 ID 추출

        Span newSpan = tracer.nextSpan().name(messageId).start(); // Span 이름을 메시지 ID로 설정
        newSpan.tag("messageId", messageId); // messageId 태그 추가
        newSpan.tag("consumer", "MEMBER"); // consumer 태그 추가

        try (Tracer.SpanInScope ws = tracer.withSpanInScope(newSpan)) {
            // SQS 메시지 처리 로직
            String topicArn = messageNode.get("TopicArn").asText();
            String messageContent = messageNode.get("Message").asText();

            log.info("[MEMBER] Received message from SQS with messageId: {}", messageId);

            // Assuming the "Message" is also a JSON string, we parse it to print as JSON object
            JsonNode message = objectMapper.readTree(messageContent);
            log.info("Message:  {}", message.toString());
        } finally {
            newSpan.finish();
        }

    }

}
