package com.recipia.member.aws;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.recipia.member.config.aws.AwsSnsConfig;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.sns.SnsClient;
import software.amazon.awssdk.services.sns.model.PublishRequest;
import software.amazon.awssdk.services.sns.model.PublishResponse;
import brave.Span;
import brave.Tracer;

import java.util.Map;

@Slf4j
@RequiredArgsConstructor
@Service
public class SnsService {

    private final SnsClient snsClient;
    private final AwsSnsConfig awsSnsConfig;
    private final ObjectMapper objectMapper;
    private final Tracer tracer;


    public PublishResponse publishNicknameToTopic(Map<String, Object> messageMap) {
        // 메시지를 JSON 형태로 변환
        String messageJson = convertMapToJson(messageMap);

        // SNS 발행 요청 생성
        PublishRequest publishRequest = PublishRequest.builder()
                .message(messageJson)
                .topicArn(awsSnsConfig.getSnsTopicNicknameChangeARN())
                .build();

        // SNS 클라이언트를 통해 메시지 발행
        PublishResponse response = snsClient.publish(publishRequest);

        // messageId 로깅
        log.info("[MEMBER] Published message to SNS with messageId: {}", response.messageId());

        // 새로운 Span 생성 및 시작
        Span newSpan = tracer.nextSpan().name(response.messageId()).start(); // Span 이름을 SNS 메시지 ID로 설정

        try (Tracer.SpanInScope ws = tracer.withSpanInScope(newSpan)) {
            newSpan.tag("messageId", String.valueOf(response)); // messageId 태그 추가
            newSpan.tag("producer", "MEMBER"); // messageId 태그 추가

            // 별도의 추가 작업 없음
        } finally {
            newSpan.finish(); // Span 완료
        }

        return response;
    }

    private String convertMapToJson(Map<String, Object> messageMap) {
        try {
            return objectMapper.writeValueAsString(messageMap);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Error converting message map to JSON", e);
        }
    }

}
