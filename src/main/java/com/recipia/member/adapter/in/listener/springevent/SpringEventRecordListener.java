package com.recipia.member.adapter.in.listener.springevent;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.recipia.member.application.port.in.MemberEventRecordUseCase;
import com.recipia.member.common.event.NicknameChangeSpringEvent;
import com.recipia.member.common.event.SignUpSpringEvent;
import com.recipia.member.domain.MemberEventRecord;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;


@RequiredArgsConstructor
@Component
public class SpringEventRecordListener {

    private final MemberEventRecordUseCase memberEventRecordUseCase;

    /**
     * 이벤트를 호출한 서비스 코드의 트랜잭션과 묶이게 된다.
     */
    @Transactional
    @EventListener
    public void eventRecordListener(NicknameChangeSpringEvent event) throws JsonProcessingException {
        MemberEventRecord memberEventRecord = MemberEventRecord.of(event.memberId(), String.valueOf(NicknameChangeSpringEvent.class));
        memberEventRecordUseCase.saveNewEventRecord(memberEventRecord);
    }

    /**
     * 회원가입 후 이벤트 발행 여부를 기록 (transactional out box pattern)
     */
    @Transactional
    @EventListener
    public void signUpListener(SignUpSpringEvent event) throws JsonProcessingException {
        MemberEventRecord memberEventRecord = MemberEventRecord.of(event.memberId(), String.valueOf(SignUpSpringEvent.class));
        memberEventRecordUseCase.saveNewEventRecord(memberEventRecord);
    }

}
