package com.recipia.member.adapter.out.persistenceAdapter.querydsl;

import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

import static com.recipia.member.hexagonal.adapter.out.persistence.QMemberEventRecordEntity.memberEventRecordEntity;


@RequiredArgsConstructor
@Repository
public class MemberEventRecordQueryRepository {

    private final JPAQueryFactory jpaQueryFactory;

    public long changePublishedToTrue(Long memberId, String snsTopic) {

        return jpaQueryFactory
                .update(memberEventRecordEntity)
                .set(memberEventRecordEntity.published, true)
                .set(memberEventRecordEntity.publishedAt, LocalDateTime.now())
                .where(
                        // 가장 최신 이벤트 가져오기 위함
                        memberEventRecordEntity.id.eq(
                                JPAExpressions
                                        .select(memberEventRecordEntity.id.max())
                                        .from(memberEventRecordEntity)
                                        .where(
                                                memberEventRecordEntity.member.id.eq(memberId),
                                                memberEventRecordEntity.snsTopic.eq(snsTopic),
                                                memberEventRecordEntity.published.isFalse()
                                        )
                        )
                )
                .execute();
    }

}
