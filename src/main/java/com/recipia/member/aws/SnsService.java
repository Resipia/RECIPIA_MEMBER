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
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.sns.SnsClient;
import software.amazon.awssdk.services.sns.model.PublishRequest;
import com.fasterxml.jackson.core.JsonProcessingException;
import software.amazon.awssdk.services.sns.model.PublishResponse;

import java.util.Map;

@XRayEnabled
@RequiredArgsConstructor
@Slf4j
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


    public void yourMethod() {
        AWSXRayRecorder recorder = AWSXRay.getGlobalRecorder();

        // 현재 활성화된 세그먼트가 있는지 확인
        Segment currentSegment = recorder.getCurrentSegmentOptional().orElse(null);
        log.debug("현재 활성화된 세그먼트: {}", currentSegment);

        boolean newSegmentCreated = false;

        if (currentSegment == null) {
            // 현재 세그먼트가 없으면 새로운 세그먼트 시작
            currentSegment = recorder.beginSegment("publishNicknameToTopicSegment");
            log.debug("새 세그먼트 생성됨: {}", currentSegment);
            newSegmentCreated = true;
        }

        try {
            // 서브세그먼트 생성
            Subsegment subsegment = recorder.beginSubsegment("publishToSns");
            log.debug("서브세그먼트 생성됨: {}", subsegment);

            try {
                // 성공 로그 기록
                subsegment.putAnnotation("MessageID", "성공 메시지 ID");
                log.debug("작업 성공: {}", "성공 메시지 ID");
            } catch (Exception e) {
                // 에러 로그 기록
                subsegment.addException(e);
                log.error("작업 중 에러 발생", e);
                throw e;
            } finally {
                // 서브세그먼트 종료
                recorder.endSubsegment();
                log.debug("서브세그먼트 종료됨: {}", subsegment);
            }
        } finally {
            // 새로운 세그먼트가 시작되었다면 종료
            if (newSegmentCreated) {
                recorder.endSegment();
                log.debug("새 세그먼트 종료됨: {}", currentSegment);
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
