package com.recipia.member.adapter.out.persistenceAdapter.querydsl;

import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.recipia.member.domain.MyPage;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import static com.querydsl.core.types.Projections.constructor;
import static com.recipia.member.adapter.out.persistence.QFollowEntity.followEntity;
import static com.recipia.member.adapter.out.persistence.QMemberEntity.memberEntity;

@RequiredArgsConstructor
@Repository
public class MyPageQueryRepository {

    private final JPAQueryFactory jpaQueryFactory;

    public MyPage viewMyPage(Long memberId) {
        return jpaQueryFactory
                .select(constructor(MyPage.class,
                        memberEntity.id,
                        memberEntity.nickname,
                        memberEntity.introduction,
                        JPAExpressions.select(followEntity.count())
                                .from(followEntity)
                                .where(followEntity.followerMember.id.eq(memberEntity.id)),
                        JPAExpressions.select(followEntity.count())
                                .from(followEntity)
                                .where(followEntity.followingMember.id.eq(memberEntity.id))))
                .from(memberEntity)
                .where(memberEntity.id.eq(memberId))
                .fetchOne();
    }
}
