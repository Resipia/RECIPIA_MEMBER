package com.recipia.member.application.service;

import brave.Tracer;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.recipia.member.application.port.in.MemberEventRecordUseCase;
import com.recipia.member.application.port.out.port.MemberEventRecordPort;
import com.recipia.member.common.utils.CustomJsonBuilder;
import com.recipia.member.common.utils.MemberStringUtils;
import com.recipia.member.config.aws.SnsConfig;
import com.recipia.member.domain.MemberEventRecord;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class MemberEventRecordService implements MemberEventRecordUseCase {

    private final MemberEventRecordPort memberEventRecordPort;
    private final SnsConfig snsConfig;
    private final CustomJsonBuilder customJsonBuilder;
    private final Tracer tracer;

    /**
     * 가장 최근에 발행 성공한 이벤트를 published = true로 업데이트
     */
    @Transactional
    @Override
    public void changePublishedToTrue(Long memberId, String topicName) {
        Long changedCount = memberEventRecordPort.changePublishedToTrue(memberId, topicName);
    }

    /**
     * 새로운 이벤트를 발행하기 전에 기존에 누락되었던 이벤트 전부 published = true로 업데이트하고,
     * 새로운 이벤트 저장
     */
    @Transactional
    @Override
    public void saveNewEventRecord(MemberEventRecord memberEventRecord) throws JsonProcessingException {

        Long memberId = memberEventRecord.getMemberId();

        // 현재 TraceID 추출
        String traceId = tracer.currentSpan().context().traceIdString();

        // attribute에 memberId 주입
        String attribute = customJsonBuilder
                .add("memberId", memberId)
                .build();

        String topicName = MemberStringUtils.extractLastPart(snsConfig.getNicknameChangeArn());

        // 이벤트 저장소에 이벤트 저장하기 전에 기존에 누락되었던(published = false) 동일한 이벤트는 전부 발행 처리(published = true)로 수정
        memberEventRecordPort.changeBeforeEventAllPublishedToTrue(memberId, topicName);

        // 이전에 발행된 이벤트가 전부 발행처리 되면 새로운 이벤트 저장
        MemberEventRecord memberEventRecordNew = MemberEventRecord.of(memberId, topicName, memberEventRecord.getEventType(), attribute, traceId, false, null);
        memberEventRecordPort.save(memberEventRecordNew);
    }


}
