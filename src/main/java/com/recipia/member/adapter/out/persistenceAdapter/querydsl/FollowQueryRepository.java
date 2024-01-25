package com.recipia.member.adapter.out.persistenceAdapter.querydsl;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.recipia.member.domain.Follow;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import static com.recipia.member.adapter.out.persistence.QFollowEntity.followEntity;

@RequiredArgsConstructor
@Repository
public class FollowQueryRepository {

    private final JPAQueryFactory jpaQueryFactory;

    /**
     * [DELETE] 팔로우 삭제
     */
    public Long deleteFollow(Follow follow) {
        return jpaQueryFactory
                .delete(followEntity)
                .where(followEntity.followerMember.id.eq(follow.getFollowerMemberId()), followEntity.followingMember.id.eq(follow.getFollowingMemberId()))
                .execute();
    }

    /**
     * [DELETE] 나를 팔로우하는 관계를 삭제
     */
    public void deleteFollowingByMemberId(Long memberId) {
        jpaQueryFactory
                .delete(followEntity)
                .where(followEntity.followingMember.id.eq(memberId))
                .execute();
    }

    /**
     * [DELETE] 내가 팔로우하는 관계를 삭제
     */
    public void deleteFollowerByMemberId(Long memberId) {
        jpaQueryFactory
                .delete(followEntity)
                .where(followEntity.followerMember.id.eq(memberId))
                .execute();
    }
}
