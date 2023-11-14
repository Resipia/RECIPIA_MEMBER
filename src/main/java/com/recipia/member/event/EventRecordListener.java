package com.recipia.member.event;

import com.fasterxml.jackson.core.JsonFactoryBuilder;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
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

import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor
@Component
public class EventRecordListener {

    private final MemberRepository memberRepository;
    private final MemberEventRecordRepository memberEventRecordRepository;
    private final AwsSnsConfig awsSnsConfig;
    private final CustomJsonBuilder customJsonBuilder;

    /**
     * 이벤트를 호출한 서비스 코드의 트랜잭션과 묶이게 된다.
     */
    @Transactional
    @EventListener
    public void listen(NicknameChangeEvent event) throws JsonProcessingException {
        // 여기서 db에 저장하는 로직 실행 (트랜잭션이 묶여있어야 함)
        Member member = memberRepository.findById(event.memberId()).orElseThrow(() -> new MemberApplicationException(ErrorCode.USER_NOT_FOUND));

        // JSON 객체 생성 및 문자열 변환
        String messageJson = customJsonBuilder.add("memberId", member.getId().toString()).build();

        String topicName = MemberStringUtils.extractLastPart(awsSnsConfig.getSnsTopicNicknameChangeARN());

        MemberEventRecord memberEventRecord = MemberEventRecord.of(
                member,
                topicName,
                "NicknameChangeEvent",
                messageJson,
                false,
                null
        );

        memberEventRecordRepository.save(memberEventRecord);
    }



}
