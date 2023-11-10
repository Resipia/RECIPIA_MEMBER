package com.recipia.member.aws;

import com.amazonaws.xray.AWSXRay;
import com.amazonaws.xray.entities.Subsegment;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.recipia.member.config.aws.AwsSnsConfig;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.sns.SnsClient;
import software.amazon.awssdk.services.sns.model.PublishRequest;
import software.amazon.awssdk.services.sns.model.PublishResponse;

import java.io.IOException;
import java.util.Map;

@RequiredArgsConstructor
@Service
public class SnsService {

    private final SnsClient snsClient;
    private final AwsSnsConfig awsSnsConfig;
    private final ObjectMapper objectMapper;

    public PublishResponse publishNicknameToTopic(Map<String, Object> messageMap) {

        // X-ray
        Subsegment subsegment = AWSXRay.beginSubsegment("publishNicknameToTopic");
        try {
            String messageJson = convertMapToJson(messageMap);
            PublishRequest publishRequest = PublishRequest.builder()
                    .message(messageJson)
                    .topicArn(awsSnsConfig.getSnsTopicNicknameChangeARN())
                    .build();

            return snsClient.publish(publishRequest);
        } catch (Exception e) {
            subsegment.addException(e);
            throw e;
        } finally {
            AWSXRay.endSubsegment();
        }

    }

    private String convertMapToJson(Map<String, Object> messageMap) {
        try {
            return objectMapper.writeValueAsString(messageMap);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Error converting message map to JSON", e);
        }
    }

}
