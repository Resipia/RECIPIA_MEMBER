package com.recipia.member.adapter.in.listener.springevent;

import brave.Tracer;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.recipia.member.adapter.out.aws.SeoulSnsService;
import com.recipia.member.common.event.SignUpSpringEvent;
import com.recipia.member.common.utils.CustomJsonBuilder;
import com.recipia.member.common.event.NicknameChangeSpringEvent;
import com.recipia.member.config.aws.SnsConfig;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Slf4j
@RequiredArgsConstructor
@Component
public class SpringEventSnsPublishListener {

    private final SeoulSnsService seoulSnsService;
    private final CustomJsonBuilder customJsonBuilder;
    private final SnsConfig snsConfig;
    private final Tracer tracer;

    /**
     * 이벤트를 호출한 서비스 코드의 트랜잭션과 묶여있지 않고 트랜잭션이 commit된 후에 동작한다.
     */
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void snsPublishListener(NicknameChangeSpringEvent event) throws JsonProcessingException {

        // 현재 TraceID 추출
        String traceId = tracer.currentSpan().context().traceIdString();

        // message에 memberId 주입
        String messageJson = customJsonBuilder
                .add("memberId", event.memberId().toString())
                .build();

        String snsArn = snsConfig.getNicknameChangeArn();
        seoulSnsService.publishSnsMessage(messageJson, traceId, snsArn);
    }

    /**
     * 회원가입시 호출되어 SNS에 메시지를 발행한다.
     */
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void signUpSnsPublishListener(SignUpSpringEvent event) throws JsonProcessingException {

        // 현재 TraceID 추출
        String traceId = tracer.currentSpan().context().traceIdString();

        // message에 memberId 주입
        String messageJson = customJsonBuilder
                .add("memberId", event.memberId().toString())
                .build();

        String snsArn = snsConfig.getSignUpArn();
        seoulSnsService.publishSnsMessage(messageJson, traceId, snsArn);
    }

}
