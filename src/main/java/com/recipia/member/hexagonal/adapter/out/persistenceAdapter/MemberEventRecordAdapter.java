package com.recipia.member.hexagonal.adapter.out.persistenceAdapter;

import com.recipia.member.hexagonal.adapter.out.persistenceAdapter.querydsl.MemberEventRecordQueryRepository;
import com.recipia.member.hexagonal.application.port.out.port.MemberEventRecordPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class MemberEventRecordAdapter implements MemberEventRecordPort {

    private final MemberEventRecordQueryRepository memberEventRecordQueryRepository;

    @Override
    public long changePublishedToTrue(Long memberId, String topicName) {
        return memberEventRecordQueryRepository.changePublishedToTrue(memberId, topicName);
    }
}
