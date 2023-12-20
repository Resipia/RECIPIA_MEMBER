package com.recipia.member.hexagonal.application.service;

import com.recipia.member.hexagonal.application.port.in.MemberEventRecordUseCase;
import com.recipia.member.hexagonal.application.port.out.port.MemberEventRecordPort;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class MemberEventRecordService implements MemberEventRecordUseCase {

    private final MemberEventRecordPort memberEventRecordPort;

    @Transactional
    @Override
    public void changePublishedToTrue(Long memberId, String topicName) {
        long changedCount = memberEventRecordPort.changePublishedToTrue(memberId, topicName);
    }
}
