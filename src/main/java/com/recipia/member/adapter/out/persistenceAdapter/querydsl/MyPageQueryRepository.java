package com.recipia.member.adapter.out.persistenceAdapter.querydsl;

import com.querydsl.core.types.ExpressionUtils;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.JPQLQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.recipia.member.domain.MyPage;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import static com.recipia.member.adapter.out.persistence.QFollowEntity.followEntity;
import static com.recipia.member.adapter.out.persistence.QMemberEntity.memberEntity;
import static com.recipia.member.adapter.out.persistence.QMemberFileEntity.memberFileEntity;

@RequiredArgsConstructor
@Repository
public class MyPageQueryRepository {

    private final JPAQueryFactory jpaQueryFactory;

    /**
     * [READ] memberId에 해당하는 멤버 정보를 조회한다.
     */
    public MyPage viewMyPage(Long memberId) {

        // 파일 경로 가져오는 서브쿼리
        JPQLQuery<String> filePathSubQuery = JPAExpressions
                .select(memberFileEntity.storedFilePath)
                .from(memberFileEntity)
                .where(memberFileEntity.memberEntity.id.eq(memberId));

        // 팔로잉 수 가져오는 서브쿼리
        JPQLQuery<Long> followingCountSubQuery = JPAExpressions
                .select(Expressions.numberTemplate(Long.class, "coalesce({0}, 0)", followEntity.count()))
                .from(followEntity)
                .where(followEntity.followerMember.id.eq(memberId));

        // 팔로워 수 가져오는 서브쿼리
        JPQLQuery<Long> followerCountSubQuery = JPAExpressions
                .select(Expressions.numberTemplate(Long.class, "coalesce({0}, 0)", followEntity.count()))
                .from(followEntity)
                .where(followEntity.followingMember.id.eq(memberId));


        return jpaQueryFactory
                .select(Projections.fields(MyPage.class,
                        memberEntity.id.as("memberId"),
                        ExpressionUtils.as(filePathSubQuery, "profileImageFilePath"),
                        memberEntity.nickname.as("nickname"),
                        memberEntity.introduction.as("introduction"),
                        ExpressionUtils.as(followingCountSubQuery, "followingCount"),
                        ExpressionUtils.as(followerCountSubQuery, "followerCount")))
                .from(memberEntity)
                .where(memberEntity.id.eq(memberId))
                .fetchOne();
    }

    /**
     * [UPDATE] 마이페이지에서 기본 정보에 해당하는 항목을 수정한다.
     */
    public Long updateMyPage(MyPage requestMyPage) {
        return jpaQueryFactory
                .update(memberEntity)
                .set(memberEntity.nickname, requestMyPage.getNickname())
                .set(memberEntity.introduction, requestMyPage.getIntroduction())
                .where(memberEntity.id.eq(requestMyPage.getMemberId()))
                .execute();
    }
}
