package com.recipia.member.aws;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.recipia.member.config.aws.AwsSnsConfig;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.sns.SnsClient;
import software.amazon.awssdk.services.sns.model.PublishRequest;
import software.amazon.awssdk.services.sns.model.PublishResponse;

import java.util.Map;

@RequiredArgsConstructor
@Service
public class SnsService {

    private final SnsClient snsClient;
    private final AwsSnsConfig awsSnsConfig;
    private final ObjectMapper objectMapper;


    public PublishResponse publishNicknameToTopic(Map<String, Object> messageMap) {
        // 메시지를 JSON 형태로 변환
        String messageJson = convertMapToJson(messageMap);

        // SNS 발행 요청 생성
        PublishRequest publishRequest = PublishRequest.builder()
                .message(messageJson)
                .topicArn(awsSnsConfig.getSnsTopicNicknameChangeARN())
                .build();

        // SNS 클라이언트를 통해 메시지 발행
        return snsClient.publish(publishRequest);
    }

    private String convertMapToJson(Map<String, Object> messageMap) {
        try {
            return objectMapper.writeValueAsString(messageMap);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Error converting message map to JSON", e);
        }
    }

}
