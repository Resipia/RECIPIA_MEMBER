package com.recipia.member.aws;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.recipia.member.config.aws.AwsSnsConfig;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.sns.SnsClient;
import software.amazon.awssdk.services.sns.model.PublishRequest;
import com.fasterxml.jackson.core.JsonProcessingException;
import software.amazon.awssdk.services.sns.model.PublishResponse;

import java.util.Map;


@RequiredArgsConstructor
@Service
public class SnsService {

    private final SnsClient snsClient;
    private final AwsSnsConfig awsSnsConfig;
    private final ObjectMapper objectMapper;

    public PublishResponse publishNicknameToTopic(Map<String, Object> messageMap) {
        String messageJson = convertMapToJson(messageMap);
        PublishRequest publishRequest = PublishRequest.builder()
                .message(messageJson)
                .topicArn(awsSnsConfig.getSnsTopicNicknameChangeARN())
                .build();

        return snsClient.publish(publishRequest);
    }


//    public void publishOneToTopic(Map<String, Object> messageMap) {
//        String messageJson = convertMapToJson(messageMap);
//        PublishRequest publishRequest = PublishRequest.builder()
//                .message(messageJson)
//                .topicArn(awsSnsConfig.getSnsTopic1ARN())
//                .build();
//
//        snsClient.publish(publishRequest);
//    }

    private String convertMapToJson(Map<String, Object> messageMap) {
        try {
            return objectMapper.writeValueAsString(messageMap);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Error converting message map to JSON", e);
        }
    }

}
