package com.recipia.member.adapter.in.listener.springevent;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.recipia.member.application.port.in.MemberEventRecordUseCase;
import com.recipia.member.common.event.MemberFollowSpringEvent;
import com.recipia.member.common.event.MemberWithdrawSpringEvent;
import com.recipia.member.common.event.NicknameChangeSpringEvent;
import com.recipia.member.common.event.SignUpSpringEvent;
import com.recipia.member.common.utils.CustomJsonBuilder;
import com.recipia.member.common.utils.MemberStringUtils;
import com.recipia.member.config.aws.SnsConfig;
import com.recipia.member.domain.MemberEventRecord;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;


/**
 * 이벤트 저장소 관련 스프링 이벤트 리스너 클래스
 */
@RequiredArgsConstructor
@Component
public class SpringEventRecordListener {

    private final MemberEventRecordUseCase memberEventRecordUseCase;
    private final SnsConfig snsConfig;

    /**
     * 이벤트를 호출한 서비스 코드의 트랜잭션과 묶이게 된다.
     */
    @Transactional
    @EventListener
    public void nicknameChangeEventRecordListener(NicknameChangeSpringEvent event) throws JsonProcessingException {
        String eventType = event.getClass().getSimpleName();
        String topicArn = snsConfig.getNicknameChangeArn();
        String topicName = MemberStringUtils.extractLastPart(topicArn);
        // 메세지 작성
        String attribute = new CustomJsonBuilder()
                .add("memberId", event.memberId())
                .build();
        MemberEventRecord memberEventRecord = MemberEventRecord.of(event.memberId(), eventType, topicName, attribute);
        memberEventRecordUseCase.saveNewEventRecord(memberEventRecord);
    }

    /**
     * 회원가입 후 이벤트 발행 여부를 기록 (transactional out box pattern)
     */
    @Transactional
    @EventListener
    public void signUpEventRecordListener(SignUpSpringEvent event) throws JsonProcessingException {
        String eventType = event.getClass().getSimpleName();
        String topicArn = snsConfig.getSignUpArn();
        String topicName = MemberStringUtils.extractLastPart(topicArn);
        // 메세지 작성
        String attribute = new CustomJsonBuilder()
                .add("memberId", event.memberId())
                .build();
        MemberEventRecord memberEventRecord = MemberEventRecord.of(event.memberId(), eventType, topicName, attribute);
        memberEventRecordUseCase.saveNewEventRecord(memberEventRecord);
    }

    /**
     * 회원 탈퇴 후 이벤트 발행 여부를 기록 (transactional out box pattern)
     */
    @Transactional
    @EventListener
    public void memberWithdrawEventRecordListener(MemberWithdrawSpringEvent event) throws JsonProcessingException {
        String eventType = event.getClass().getSimpleName();
        String topicArn = snsConfig.getMemberWithdrawArn();
        String topicName = MemberStringUtils.extractLastPart(topicArn);
        // 메세지 작성
        String attribute = new CustomJsonBuilder()
                .add("memberId", event.memberId())
                .build();
        MemberEventRecord memberEventRecord = MemberEventRecord.of(event.memberId(), eventType, topicName, attribute);
        memberEventRecordUseCase.saveNewEventRecord(memberEventRecord);
    }

    /**
     * 회원 팔로우 성공 후 이벤트 발행 여부를 기록 (transactional out box pattern)
     */
    @Transactional
    @EventListener
    public void memberFollowEventRecordListener(MemberFollowSpringEvent event) throws JsonProcessingException {
        String eventType = event.getClass().getSimpleName();
        String topicArn = snsConfig.getMemberFollowArn();
        String topicName = MemberStringUtils.extractLastPart(topicArn);
        // 메세지 작성
        String attribute = new CustomJsonBuilder()
                .add("followerId", event.followerId())
                .add("targetMemberId", event.targetMemberId())
                .build();
        MemberEventRecord memberEventRecord = MemberEventRecord.of(event.targetMemberId(), eventType, topicName, attribute);
        memberEventRecordUseCase.saveNewEventRecord(memberEventRecord);
    }

}
