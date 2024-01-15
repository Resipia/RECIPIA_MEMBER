package com.recipia.member.adapter.out.persistenceAdapter;

import com.recipia.member.adapter.out.persistence.MemberEventRecordEntity;
import com.recipia.member.adapter.out.persistenceAdapter.querydsl.MemberEventRecordQueryRepository;
import com.recipia.member.application.port.out.port.MemberEventRecordPort;
import com.recipia.member.domain.MemberEventRecord;
import com.recipia.member.domain.converter.MemberEventRecordConverter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class MemberEventRecordAdapter implements MemberEventRecordPort {

    private final MemberEventRecordQueryRepository memberEventRecordQueryRepository;
    private final MemberEventRecordRepository memberEventRecordRepository;

    /**
     * [UPDATE] 가장 최근에 발행 성공한 이벤트를 published = true로 업데이트한다.
     */
    @Override
    public Long changePublishedToTrue(Long memberId, String topicName) {
        return memberEventRecordQueryRepository.changePublishedToTrue(memberId, topicName);
    }

    /**
     * [UPDATE] 새로운 이벤트를 발행하기 전에 기존에 누락되었던 이벤트 전부 published = true로 업데이트한다.
     */
    @Override
    public Long changeBeforeEventAllPublishedToTrue(Long memberId, String topicName) {
        return memberEventRecordQueryRepository.changeBeforeEventAllPublishedToTrue(memberId, topicName);
    }

    /**
     * [CREATE] 이벤트 저장
     */
    @Override
    public void save(MemberEventRecord memberEventRecordNew) {
        MemberEventRecordEntity memberEventRecordEntity = MemberEventRecordConverter.domainToEntity(memberEventRecordNew);
        memberEventRecordRepository.save(memberEventRecordEntity);
    }
}
