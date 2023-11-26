package com.recipia.member.springevent;

import brave.Tracer;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.recipia.member.config.aws.AwsSnsConfig;
import com.recipia.member.domain.Member;
import com.recipia.member.domain.event.MemberEventRecord;
import com.recipia.member.exception.ErrorCode;
import com.recipia.member.exception.MemberApplicationException;
import com.recipia.member.repository.MemberEventRecordRepository;
import com.recipia.member.repository.MemberRepository;
import com.recipia.member.utils.CustomJsonBuilder;
import com.recipia.member.utils.MemberStringUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;


@RequiredArgsConstructor
@Component
public class SpringEventRecordListener {

    private final MemberRepository memberRepository;
    private final MemberEventRecordRepository memberEventRecordRepository;
    private final AwsSnsConfig awsSnsConfig;
    private final CustomJsonBuilder customJsonBuilder;
    private final Tracer tracer;

    /**
     * 이벤트를 호출한 서비스 코드의 트랜잭션과 묶이게 된다.
     */
    @Transactional
    @EventListener
    public void eventRecordListener(NicknameChangeSpringEvent event) throws JsonProcessingException {
        Member member = memberRepository.findById(event.memberId()).orElseThrow(() -> new MemberApplicationException(ErrorCode.USER_NOT_FOUND));

        // 현재 TraceID 추출
        String traceId = tracer.currentSpan().context().traceIdString();

        // message에 memberId, traceId 주입
        String messageJson = customJsonBuilder
                .add("memberId", event.memberId().toString())
                .build();

        String topicName = MemberStringUtils.extractLastPart(awsSnsConfig.getSnsTopicNicknameChangeARN());

        MemberEventRecord memberEventRecord = MemberEventRecord.of(
                member,
                topicName,
                "NicknameChangeEvent",
                messageJson,
                traceId,
                false,
                null
        );

        memberEventRecordRepository.save(memberEventRecord);
    }



}
