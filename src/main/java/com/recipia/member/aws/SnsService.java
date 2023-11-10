package com.recipia.member.aws;

import com.amazonaws.xray.AWSXRay;
import com.amazonaws.xray.entities.Subsegment;
import com.amazonaws.xray.spring.aop.XRayEnabled;
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

@XRayEnabled
@RequiredArgsConstructor
@Service
public class SnsService {

    private final SnsClient snsClient;
    private final AwsSnsConfig awsSnsConfig;
    private final ObjectMapper objectMapper;

//    public PublishResponse publishNicknameToTopic(Map<String, Object> messageMap) {
//
//        // X-ray
//        Subsegment subsegment = AWSXRay.beginSubsegment("publishNicknameToTopic");
//        try {
//            String messageJson = convertMapToJson(messageMap);
//            PublishRequest publishRequest = PublishRequest.builder()
//                    .message(messageJson)
//                    .topicArn(awsSnsConfig.getSnsTopicNicknameChangeARN())
//                    .build();
//
//            return snsClient.publish(publishRequest);
//        } catch (Exception e) {
//            subsegment.addException(e);
//            throw e;
//        } finally {
//            AWSXRay.endSubsegment();
//        }
//
//    }

    public PublishResponse publishNicknameToTopic(Map<String, Object> messageMap) {
        Subsegment subsegment = AWSXRay.beginSubsegment("Publish SNS Message");
        try {
            // 메시지를 JSON 형태로 변환
            String messageJson = convertMapToJson(messageMap);

            // SNS 발행 요청 생성
            PublishRequest publishRequest = PublishRequest.builder()
                    .message(messageJson)
                    .topicArn(awsSnsConfig.getSnsTopicNicknameChangeARN())
                    .build();

            // SNS 클라이언트를 통해 메시지 발행
            return snsClient.publish(publishRequest);
        } catch (Exception e) {
            // 오류 발생 시 X-Ray에 오류 정보 추가
            AWSXRay.getCurrentSegment().addException(e);
            throw e;
        } finally {
            // X-Ray 서브세그먼트 종료
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
