package com.recipia.member.adapter.out.persistenceAdapter.querydsl;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.recipia.member.adapter.out.persistence.constant.MemberStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import static com.recipia.member.adapter.out.persistence.QMemberEntity.memberEntity;
import static com.recipia.member.adapter.out.persistence.QMemberEventRecordEntity.memberEventRecordEntity;

@RequiredArgsConstructor
@Repository
public class MemberQueryRepository {

    private final JPAQueryFactory jpaQueryFactory;

    public Long deactivateMemberByMemberId(Long memberId) {
        return jpaQueryFactory
                .update(memberEntity)
                .set(memberEntity.status, MemberStatus.DEACTIVATED)
                .where(memberEntity.id.eq(memberId))
                .execute();
    }

}
