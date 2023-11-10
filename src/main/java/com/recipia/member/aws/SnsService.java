package com.recipia.member.aws;

import com.amazonaws.xray.AWSXRayRecorder;
import com.amazonaws.xray.AWSXRayRecorderBuilder;
import com.amazonaws.xray.emitters.Emitter;
import com.amazonaws.xray.entities.Segment;
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


    public PublishResponse publishNicknameToTopic(Map<String, Object> messageMap) throws IOException {
        String messageJson = convertMapToJson(messageMap);
        PublishRequest publishRequest = PublishRequest.builder()
                .message(messageJson)
                .topicArn(awsSnsConfig.getSnsTopicNicknameChangeARN())
                .build();

//        AWSXRayRecorder recorder = AWSXRay.getGlobalRecorder();
        AWSXRayRecorder recorder = AWSXRayRecorderBuilder.standard()
                .withEmitter(Emitter.create())
                .build();

        // 현재 활성화된 세그먼트가 있는지 확인
        Segment currentSegment = recorder.getCurrentSegmentOptional().orElse(null);
        boolean newSegmentCreated = false;

        if (currentSegment == null) {
            // 현재 세그먼트가 없으면 새로운 세그먼트 시작
            currentSegment = recorder.beginSegment("publishNicknameToTopicSegment");
            newSegmentCreated = true;
        }

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
        } finally {
            // 새로운 세그먼트가 시작되었다면 종료
            if (newSegmentCreated) {
                recorder.endSegment();
            }
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
