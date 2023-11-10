package com.recipia.member.aws;

import com.amazonaws.xray.AWSXRay;
import com.amazonaws.xray.AWSXRayRecorder;
import com.amazonaws.xray.entities.Segment;
import com.amazonaws.xray.entities.Subsegment;
import com.amazonaws.xray.exceptions.SegmentNotFoundException;
import com.amazonaws.xray.spring.aop.XRayEnabled;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.recipia.member.config.aws.AwsSnsConfig;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.sns.SnsClient;
import software.amazon.awssdk.services.sns.model.PublishRequest;
import com.fasterxml.jackson.core.JsonProcessingException;
import software.amazon.awssdk.services.sns.model.PublishResponse;

import java.util.Map;

@XRayEnabled
@RequiredArgsConstructor
@Service
public class SnsService {

    private final SnsClient snsClient;
    private final AwsSnsConfig awsSnsConfig;
    private final ObjectMapper objectMapper;

//
//    public PublishResponse publishNicknameToTopic(Map<String, Object> messageMap) {
//
//        String messageJson = convertMapToJson(messageMap);
//        PublishRequest publishRequest = PublishRequest.builder()
//                .message(messageJson)
//                .topicArn(awsSnsConfig.getSnsTopicNicknameChangeARN())
//                .build();
//
//        // 부모 세그먼트 생성
//        Segment segment = AWSXRay.beginSegment("publishNicknameToTopicSegment");
//        try {
//            // 여기서 서브세그먼트 생성
//            Subsegment subsegment = AWSXRay.beginSubsegment("publishToSns");
//
//            try {
//                return snsClient.publish(publishRequest);
//            }catch (Exception e) {
//                subsegment.addException(e);
//                throw e;
//            } finally {
//                // 서브세그먼트 종료
//                AWSXRay.endSubsegment();
//            }
//        } finally {
//
//            // 부모 세그먼트 종료
//            AWSXRay.endSegment();
//        }
//    }


    public PublishResponse publishNicknameToTopic(Map<String, Object> messageMap) {
        String messageJson = convertMapToJson(messageMap);
        PublishRequest publishRequest = PublishRequest.builder()
                .message(messageJson)
                .topicArn(awsSnsConfig.getSnsTopicNicknameChangeARN())
                .build();

        AWSXRayRecorder recorder = AWSXRay.getGlobalRecorder();
        Segment segment = recorder.beginSegment("publishNicknameToTopicSegment");

        try {
            // 서브세그먼트 생성
            Subsegment subsegment = recorder.beginSubsegment("publishToSns");

            try {
                // 실제 SNS 발행
                PublishResponse response = snsClient.publish(publishRequest);

                // 성공한 응답의 Message ID만 추적 로그로 남김
                subsegment.putAnnotation("MessageID", response.messageId());
                return response;
            } catch (Exception e) {
                // 에러 추적
                subsegment.addException(e);
                throw e;
            } finally {
                // 서브세그먼트 종료
                recorder.endSubsegment();
            }
        } catch (SegmentNotFoundException e) {
            // 세그먼트를 찾을 수 없는 경우의 예외 처리
            // 로그 찍기 또는 복구 로직
        } finally {
            // 부모 세그먼트 종료
            recorder.endSegment();
        }
        return null;
    }


    private String convertMapToJson(Map<String, Object> messageMap) {
        try {
            return objectMapper.writeValueAsString(messageMap);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Error converting message map to JSON", e);
        }
    }

}
