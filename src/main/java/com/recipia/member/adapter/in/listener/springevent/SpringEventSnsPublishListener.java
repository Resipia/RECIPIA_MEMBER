package com.recipia.member.adapter.in.listener.springevent;

import brave.Tracer;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.recipia.member.adapter.out.aws.SeoulSnsService;
import com.recipia.member.common.event.MemberFollowSpringEvent;
import com.recipia.member.common.event.MemberWithdrawSpringEvent;
import com.recipia.member.common.event.NicknameChangeSpringEvent;
import com.recipia.member.common.event.SignUpSpringEvent;
import com.recipia.member.common.utils.CustomJsonBuilder;
import com.recipia.member.config.aws.SnsConfig;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

/**
 * SNS 발행 관련 스프링 이벤트 리스너 클래스
 */
@Slf4j
@RequiredArgsConstructor
@Component
public class SpringEventSnsPublishListener {

    private final SeoulSnsService seoulSnsService;
    private final SnsConfig snsConfig;
    private final Tracer tracer;

    /**
     * SNS 메시지 생성 후 SNS 발행 메서드 호출 담당
     * 이벤트를 호출한 서비스 코드의 트랜잭션과 묶여있지 않고 트랜잭션이 commit된 후에 동작한다.
     */
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void snsPublishListener(NicknameChangeSpringEvent event) throws JsonProcessingException {

        // 현재 TraceID 추출
        String traceId = tracer.currentSpan().context().traceIdString();

        // message에 memberId 주입
        String messageJson = new CustomJsonBuilder()
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
        String messageJson = new CustomJsonBuilder()
                .add("memberId", event.memberId().toString())
                .build();

        String snsArn = snsConfig.getSignUpArn();
        seoulSnsService.publishSnsMessage(messageJson, traceId, snsArn);
    }

    /**
     * 회원 탈퇴 시 호출되어 SNS에 메시지를 발행한다.
     */
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void memberWithdrawSnsPublishListener(MemberWithdrawSpringEvent event) throws JsonProcessingException {
        // 현재 TraceID 추출
        String traceId = tracer.currentSpan().context().traceIdString();

        // message에 memberId 주입
        String messageJson = new CustomJsonBuilder()
                .add("memberId", event.memberId().toString())
                .build();

        String snsArn = snsConfig.getMemberWithdrawArn();
        seoulSnsService.publishSnsMessage(messageJson, traceId, snsArn);
    }

    /**
     * 회원 팔로우 시 호출되어 SNS에 메시지를 발행한다.
     */
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void memberFollowSnsPublishListener(MemberFollowSpringEvent event) throws JsonProcessingException {
        // 현재 TraceID 추출
        String traceId = tracer.currentSpan().context().traceIdString();

        // message에 followerId, targetMemberId 주입
        String messageJson = new CustomJsonBuilder()
                .add("followerId", event.followerId().toString())
                .add("targetMemberId", event.targetMemberId().toString())
                .build();

        String snsArn = snsConfig.getMemberFollowArn();
        seoulSnsService.publishSnsMessage(messageJson, traceId, snsArn);
    }

}
