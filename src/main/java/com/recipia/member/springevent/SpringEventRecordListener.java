package com.recipia.member.springevent;

import brave.Tracer;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.recipia.member.config.aws.AwsSnsConfig;
import com.recipia.member.hexagonal.adapter.out.persistence.member.Member;
import com.recipia.member.domain.event.MemberEventRecord;
import com.recipia.member.exception.ErrorCode;
import com.recipia.member.exception.MemberApplicationException;
import com.recipia.member.repository.MemberEventRecordRepository;
import com.recipia.member.hexagonal.adapter.out.persistenceAdapter.MemberRepository;
import com.recipia.member.utils.CustomJsonBuilder;
import com.recipia.member.utils.MemberStringUtils;
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

        // attribute에 memberId 주입
        String attribute = customJsonBuilder
                .add("memberId", member.getId().toString())
                .build();

        String topicName = MemberStringUtils.extractLastPart(awsSnsConfig.getSnsTopicNicknameChangeARN());

        List<MemberEventRecord> memberEventFalseList = memberEventRecordRepository.findByMember_IdAndSnsTopicAndPublished(member.getId(), topicName, false);

        // forEach 메소드는 리스트가 비어있을 경우, 아무 작업도 수행하지 않기 때문에,
        // 빈 리스트에 대한 명시적 체크는 불필요하다.
        memberEventFalseList.forEach(MemberEventRecord::changePublishedToTrue);

        MemberEventRecord memberEventRecord = MemberEventRecord.of(
                member,
                topicName,
                "NicknameChangeEvent",
                attribute,
                traceId,
                false,
                null
        );

        memberEventRecordRepository.save(memberEventRecord);
    }



}
