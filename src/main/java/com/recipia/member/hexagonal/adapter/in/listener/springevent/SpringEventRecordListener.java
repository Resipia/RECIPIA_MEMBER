package com.recipia.member.hexagonal.adapter.in.listener.springevent;

import brave.Tracer;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.recipia.member.hexagonal.config.aws.SnsConfig;
import com.recipia.member.hexagonal.adapter.out.persistence.MemberEventRecordEntity;
import com.recipia.member.hexagonal.adapter.out.persistence.MemberEntity;
import com.recipia.member.hexagonal.common.exception.ErrorCode;
import com.recipia.member.hexagonal.common.exception.MemberApplicationException;
import com.recipia.member.hexagonal.adapter.out.persistenceAdapter.MemberEventRecordRepository;
import com.recipia.member.hexagonal.adapter.out.persistenceAdapter.MemberRepository;
import com.recipia.member.hexagonal.common.utils.CustomJsonBuilder;
import com.recipia.member.hexagonal.common.utils.MemberStringUtils;
import com.recipia.member.hexagonal.common.event.NicknameChangeSpringEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@RequiredArgsConstructor
@Component
public class SpringEventRecordListener {

    private final MemberRepository memberRepository;
    private final MemberEventRecordRepository memberEventRecordRepository;
    private final SnsConfig snsConfig;
    private final CustomJsonBuilder customJsonBuilder;
    private final Tracer tracer;

    /**
     * 이벤트를 호출한 서비스 코드의 트랜잭션과 묶이게 된다.
     */
    @Transactional
    @EventListener
    public void eventRecordListener(NicknameChangeSpringEvent event) throws JsonProcessingException {

        MemberEntity member = memberRepository.findById(event.memberId()).orElseThrow(() -> new MemberApplicationException(ErrorCode.USER_NOT_FOUND));

        // 현재 TraceID 추출
        String traceId = tracer.currentSpan().context().traceIdString();

        // attribute에 memberId 주입
        String attribute = customJsonBuilder
                .add("memberId", member.getId().toString())
                .build();

        String topicName = MemberStringUtils.extractLastPart(snsConfig.getSnsTopicNicknameChangeARN());

        List<MemberEventRecordEntity> memberEventFalseList = memberEventRecordRepository.findByMember_IdAndSnsTopicAndPublished(member.getId(), topicName, false);

        // forEach 메소드는 리스트가 비어있을 경우, 아무 작업도 수행하지 않기 때문에,
        // 빈 리스트에 대한 명시적 체크는 불필요하다.
        memberEventFalseList.forEach(MemberEventRecordEntity::changePublishedToTrue);

        MemberEventRecordEntity memberEventRecordEntity = MemberEventRecordEntity.of(
                member,
                topicName,
                "NicknameChangeEvent",
                attribute,
                traceId,
                false,
                null
        );

        memberEventRecordRepository.save(memberEventRecordEntity);
    }



}