package com.recipia.member.domain.converter;

import com.recipia.member.adapter.out.persistence.MemberEntity;
import com.recipia.member.adapter.out.persistence.MemberEventRecordEntity;
import com.recipia.member.domain.MemberEventRecord;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * dto, entity와 domain을 변환해주는 로직과
 * domain을 entity로 변환해주는 로직을 담당
 */
@RequiredArgsConstructor
@Component
public class MemberEventRecordConverter {

    public static MemberEventRecordEntity domainToEntity(MemberEventRecord domain) {
        MemberEntity memberEntity = MemberEntity.of(domain.getMemberId());

        return MemberEventRecordEntity.of(
                memberEntity,
                domain.getSnsTopic(),
                domain.getEventType(),
                domain.getAttribute(),
                domain.getTraceId(),
                domain.isPublished(),
                domain.getPublishedAt()
        );
    }

}
