package com.recipia.member.adapter.in.listener.springevent;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.recipia.member.application.port.in.MemberEventRecordUseCase;
import com.recipia.member.common.event.MemberWithdrawSpringEvent;
import com.recipia.member.common.event.NicknameChangeSpringEvent;
import com.recipia.member.common.event.SignUpSpringEvent;
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

    /**
     * 이벤트를 호출한 서비스 코드의 트랜잭션과 묶이게 된다.
     */
    @Transactional
    @EventListener
    public void nicknameChangeEventRecordListener(NicknameChangeSpringEvent event) throws JsonProcessingException {
        MemberEventRecord memberEventRecord = MemberEventRecord.of(event.memberId(), String.valueOf(NicknameChangeSpringEvent.class));
        memberEventRecordUseCase.saveNewEventRecord(memberEventRecord);
    }

    /**
     * 회원가입 후 이벤트 발행 여부를 기록 (transactional out box pattern)
     */
    @Transactional
    @EventListener
    public void signUpEventRecordListener(SignUpSpringEvent event) throws JsonProcessingException {
        MemberEventRecord memberEventRecord = MemberEventRecord.of(event.memberId(), String.valueOf(SignUpSpringEvent.class));
        memberEventRecordUseCase.saveNewEventRecord(memberEventRecord);
    }

    /**
     * 회원 탈퇴 후 이벤트 발행 여부를 기록 (transactional out box pattern)
     */
    @Transactional
    @EventListener
    public void memberWithdrawEventRecordListener(MemberWithdrawSpringEvent event) throws JsonProcessingException {
        String eventType = event.getClass().getSimpleName();
        MemberEventRecord memberEventRecord = MemberEventRecord.of(event.memberId(), eventType);
        memberEventRecordUseCase.saveNewEventRecord(memberEventRecord);
    }

}
