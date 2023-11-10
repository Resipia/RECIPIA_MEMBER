package com.recipia.member.aws;

import com.amazonaws.xray.AWSXRay;
import com.amazonaws.xray.entities.Subsegment;
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

        // X-ray에서 새로운 Subsegment 시작
        Subsegment subsegment = AWSXRay.beginSubsegment("publishNicknameToTopic");


        try {
            String messageJson = convertMapToJson(messageMap);
            PublishRequest publishRequest = PublishRequest.builder()
                    .message(messageJson)
                    .topicArn(awsSnsConfig.getSnsTopicNicknameChangeARN())
                    .build();

            return snsClient.publish(publishRequest);
        } catch (Exception e) {
            // 예외가 발생한 경우, Subsegment에 예외 정보를 추가
            subsegment.addException(e);
            throw e;
        } finally {
            // Subsegment를 종료.
            // 이는 이 코드 블록의 추적이 완료되었음을 의미
            AWSXRay.endSubsegment();

        }

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
