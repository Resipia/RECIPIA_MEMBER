package com.recipia.member.hexagonal.adapter.in.listener.springevent;

import brave.Tracer;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.recipia.member.hexagonal.adapter.out.aws.SnsService;
import com.recipia.member.hexagonal.common.utils.CustomJsonBuilder;
import com.recipia.member.hexagonal.common.event.NicknameChangeSpringEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Slf4j
@RequiredArgsConstructor
@Component
public class SpringEventSnsPublishListener {

    private final SnsService snsService;
    private final CustomJsonBuilder customJsonBuilder;
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

        snsService.publishNicknameToTopic(messageJson, traceId);
    }

}
