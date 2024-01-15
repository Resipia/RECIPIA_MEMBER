package com.recipia.member.adapter.out.persistenceAdapter.querydsl;

import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

import static com.recipia.member.adapter.out.persistence.QMemberEventRecordEntity.memberEventRecordEntity;

@RequiredArgsConstructor
@Repository
public class MemberEventRecordQueryRepository {

    private final JPAQueryFactory jpaQueryFactory;

    /**
     * [UPDATE] 가장 최근에 발행 성공한 이벤트를 published = true로 업데이트한다.
     */
    public Long changePublishedToTrue(Long memberId, String snsTopic) {
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

    /**
     * [UPDATE] 새로운 이벤트를 발행하기 전에 기존에 누락되었던 이벤트 전부 published = true로 업데이트한다.
     */
    public Long changeBeforeEventAllPublishedToTrue(Long memberId, String topicName) {
        return  jpaQueryFactory
                .update(memberEventRecordEntity)
                .set(memberEventRecordEntity.published, true)
                .set(memberEventRecordEntity.publishedAt, LocalDateTime.now())
                .where(
                        memberEventRecordEntity.member.id.eq(memberId),
                        memberEventRecordEntity.snsTopic.eq(topicName),
                        memberEventRecordEntity.published.isFalse()
                )
                .execute();
    }
}
