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
import brave.Tracer;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@RequiredArgsConstructor
@Service
public class SnsService {

    private final SnsClient snsClient;
    private final AwsSnsConfig awsSnsConfig;
    private final ObjectMapper objectMapper;
    private final Tracer tracer;

    public PublishResponse publishNicknameToTopic(String message) {

        // 현재 TraceID 추출
        String traceId = tracer.currentSpan().context().traceIdString();

        Map<String, Object> messageMap = new HashMap<>();
        messageMap.put("traceId", traceId); // TraceID를 메시지에 추가
        messageMap.put("message", message);

        // SNS 발행 요청 생성
        PublishRequest publishRequest = PublishRequest.builder()
                .message(convertMapToJson(messageMap))
                .topicArn(awsSnsConfig.getSnsTopicNicknameChangeARN())
                .build();

        // SNS 클라이언트를 통해 메시지 발행
        PublishResponse response = snsClient.publish(publishRequest);

        // messageId 로깅
        log.info("[MEMBER] Published message to SNS with messageId: {}", response.messageId());

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
