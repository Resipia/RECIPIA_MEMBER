package com.recipia.member.adapter.out.aws;

import brave.Span;
import brave.Tracer;
import com.recipia.member.common.exception.ErrorCode;
import com.recipia.member.common.exception.MemberApplicationException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.sns.SnsClient;
import software.amazon.awssdk.services.sns.model.MessageAttributeValue;
import software.amazon.awssdk.services.sns.model.PublishRequest;
import software.amazon.awssdk.services.sns.model.PublishResponse;

import java.util.HashMap;
import java.util.Map;

/**
 * 서울 리전 SNS 발행 서비스
 */
@RequiredArgsConstructor
@Slf4j
@Service
public class SeoulSnsService {

    private final SnsClient snsClient;
    private final Tracer tracer;


    public PublishResponse publishSnsMessage(String message, String traceId, String snsArn) {

        // SNS 발행 Span 생성
        Span span = tracer.nextSpan().name("SNS Publish").start();

        try (Tracer.SpanInScope ws = tracer.withSpanInScope(span)) {

            Map<String, MessageAttributeValue> messageAttributes = new HashMap<>();
            messageAttributes.put("traceId", MessageAttributeValue.builder()
                    .dataType("String")
                    .stringValue(traceId)
                    .build());

            // SNS 발행 요청 생성
            PublishRequest publishRequest = PublishRequest.builder()
                    .message(message)
                    .messageAttributes(messageAttributes)
                    .topicArn(snsArn)
                    .build();

            // SNS 클라이언트를 통해 메시지 발행
            PublishResponse response = snsClient.publish(publishRequest);

            // messageId 로깅
            log.info("[MEMBER] Published message to SNS with messageId: {}", response.messageId());
            return response;
        } catch (Exception e) {
            span.tag("error", e.toString()); // 오류 태깅
            throw new MemberApplicationException(ErrorCode.AWS_SNS_CLIENT);
        } finally {
            span.finish(); // Span 종료
        }
    }

}