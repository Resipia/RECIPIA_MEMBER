package com.recipia.member.adapter.out.persistenceAdapter.querydsl;

import com.querydsl.core.types.ExpressionUtils;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.JPQLQuery;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.recipia.member.adapter.in.web.dto.response.FollowListResponseDto;
import com.recipia.member.adapter.out.persistence.constant.MemberStatus;
import com.recipia.member.domain.MyPage;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

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
                .where(memberFileEntity.memberEntity.id.eq(memberId), memberFileEntity.delYn.eq("N"));

        // 팔로잉 수 가져오는 서브쿼리
        JPQLQuery<Long> followingCountSubQuery = JPAExpressions
                .select(Expressions.numberTemplate(Long.class, "coalesce({0}, 0)", followEntity.count()))
                .from(followEntity)
                .where(followEntity.followerMember.id.eq(memberId), followEntity.followerMember.status.in(MemberStatus.ACTIVE));

        // 팔로워 수 가져오는 서브쿼리
        JPQLQuery<Long> followerCountSubQuery = JPAExpressions
                .select(Expressions.numberTemplate(Long.class, "coalesce({0}, 0)", followEntity.count()))
                .from(followEntity)
                .where(followEntity.followingMember.id.eq(memberId), followEntity.followingMember.status.in(MemberStatus.ACTIVE));

        return jpaQueryFactory
                .select(Projections.fields(MyPage.class,
                        memberEntity.id.as("memberId"),
                        memberEntity.birth.as("birth"),
                        memberEntity.gender.as("gender"),
                        ExpressionUtils.as(filePathSubQuery, "imageFilePath"),
                        memberEntity.nickname.as("nickname"),
                        memberEntity.introduction.as("introduction"),
                        ExpressionUtils.as(followingCountSubQuery, "followingCount"),
                        ExpressionUtils.as(followerCountSubQuery, "followerCount")))
                .from(memberEntity)
//                .leftJoin(memberEntity.memberFileEntity) // 외부 조인 사용
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
                .set(memberEntity.birth, requestMyPage.getBirth())
                .set(memberEntity.gender, requestMyPage.getGender())
                .set(memberEntity.updateDateTime, LocalDateTime.now())
                .where(memberEntity.id.eq(requestMyPage.getMemberId()))
                .execute();
    }

    /**
     * [READ] targetMemberId가 팔로우하거나 targetMemberId를 팔로우하고있는 회원의 목록 가져오기
     */
    public Page<FollowListResponseDto> getFollowingList(Long targetMemberId, Long loggedMemberId, String type, Pageable pageable) {

        // 프로필 이미지 파일 경로 가져오는 서브쿼리
        // 여기서 memberFileEntity.memberEntity.id는 메인 쿼리의 memberEntity.id와 연결되어야 한다.
        JPQLQuery<String> filePathSubQuery = JPAExpressions
                .select(memberFileEntity.storedFilePath)
                .from(memberFileEntity)
                .where(memberFileEntity.memberEntity.id.eq(memberEntity.id)); // Correlated Subquery 사용

        // 로그인한 사용자가 팔로우하고 있는 멤버의 followId 가져오는 서브쿼리
        JPQLQuery<Long> followIdSubQuery = JPAExpressions
                .select(followEntity.id)
                .from(followEntity)
                .where(followEntity.followerMember.id.eq(loggedMemberId)
                        .and(followEntity.followingMember.id.eq(memberEntity.id)));

        // 메인 쿼리
        JPAQuery<FollowListResponseDto> query = jpaQueryFactory
                .select(Projections.fields(FollowListResponseDto.class,
                        memberEntity.id.as("memberId"),
                        memberEntity.nickname.as("nickname"),
                        ExpressionUtils.as(filePathSubQuery, "imageFullPath"),
                        ExpressionUtils.as(followIdSubQuery, "followId"),
                        memberEntity.id.eq(loggedMemberId).as("isMe")
                ))
                .from(followEntity);


        // type에 따른 조건 분기
        if ("following".equals(type)) {
            query.join(followEntity.followingMember, memberEntity)
                    .where(followEntity.followerMember.id.eq(targetMemberId));
        } else if ("follower".equals(type)) {
            query.join(followEntity.followerMember, memberEntity)
                    .where(followEntity.followingMember.id.eq(targetMemberId));
        }

        // 공통 조건 및 페이징 처리
        query.where(followEntity.followerMember.status.in(MemberStatus.ACTIVE))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize());


        List<FollowListResponseDto> results = query.fetch();

        Long totalCount = Optional.ofNullable(jpaQueryFactory
                        .select(followEntity.id.count())
                        .from(followEntity)
                        // type에 따른 카운트 조건 분기
                        .where("following".equals(type) ?
                                        followEntity.followerMember.id.eq(targetMemberId) :
                                        followEntity.followingMember.id.eq(targetMemberId),
                                followEntity.followerMember.status.in(MemberStatus.ACTIVE))
                        .fetchOne())
                .orElse(0L);

        return new PageImpl<>(results, pageable, totalCount);
    }

    /**
     * [READ] targetMemberId에 해당하는 마이페이지 정보를 조회한다.
     */
    public MyPage viewOtherMyPage(Long memberId, Long targetMemberId) {


        // 파일 경로 가져오는 서브쿼리
        JPQLQuery<String> filePathSubQuery = JPAExpressions
                .select(memberFileEntity.storedFilePath)
                .from(memberFileEntity)
                .where(memberFileEntity.memberEntity.id.eq(targetMemberId), memberFileEntity.delYn.eq("N"));

        // 팔로잉 수 가져오는 서브쿼리
        JPQLQuery<Long> followingCountSubQuery = JPAExpressions
                .select(Expressions.numberTemplate(Long.class, "coalesce({0}, 0)", followEntity.count()))
                .from(followEntity)
                .where(followEntity.followerMember.id.eq(targetMemberId), followEntity.followerMember.status.in(MemberStatus.ACTIVE));

        // 팔로워 수 가져오는 서브쿼리
        JPQLQuery<Long> followerCountSubQuery = JPAExpressions
                .select(Expressions.numberTemplate(Long.class, "coalesce({0}, 0)", followEntity.count()))
                .from(followEntity)
                .where(followEntity.followingMember.id.eq(targetMemberId), followEntity.followingMember.status.in(MemberStatus.ACTIVE));

        // 내가 팔로우 하고있는 회원인지 가져오는 서브쿼리
        JPQLQuery<Long> followIdSubQuery = JPAExpressions
                .select(followEntity.id)
                .from(followEntity)
                .where(followEntity.followerMember.id.eq(memberId), followEntity.followingMember.id.eq(targetMemberId));

        return jpaQueryFactory
                .select(Projections.fields(MyPage.class,
                        memberEntity.id.as("memberId"),
                        memberEntity.birth.as("birth"),
                        memberEntity.gender.as("gender"),
                        ExpressionUtils.as(filePathSubQuery, "imageFilePath"),
                        memberEntity.nickname.as("nickname"),
                        memberEntity.introduction.as("introduction"),
                        ExpressionUtils.as(followingCountSubQuery, "followingCount"),
                        ExpressionUtils.as(followIdSubQuery, "followId"),
                        ExpressionUtils.as(followerCountSubQuery, "followerCount")))
                .from(memberEntity)
                .leftJoin(memberEntity.memberFileEntity) // 외부 조인 사용
                .where(memberEntity.id.eq(targetMemberId))
                .fetchOne();
    }
}
