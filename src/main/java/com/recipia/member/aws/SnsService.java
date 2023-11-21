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

import java.util.HashMap;
import java.util.Map;

@Slf4j
@RequiredArgsConstructor
@Service
public class SnsService {

    private final SnsClient snsClient;
    private final AwsSnsConfig awsSnsConfig;

    public PublishResponse publishNicknameToTopic(String message) {

        // SNS 발행 요청 생성
        PublishRequest publishRequest = PublishRequest.builder()
                .message(message)
                .topicArn(awsSnsConfig.getSnsTopicNicknameChangeARN())
                .build();

        // SNS 클라이언트를 통해 메시지 발행
        PublishResponse response = snsClient.publish(publishRequest);

        // messageId 로깅
        log.info("[MEMBER] Published message to SNS with messageId: {}", response.messageId());
        return response;
    }

}
