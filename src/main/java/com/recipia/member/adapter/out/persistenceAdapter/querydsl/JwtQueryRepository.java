package com.recipia.member.adapter.out.persistenceAdapter.querydsl;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import static com.recipia.member.adapter.out.persistence.QJwtEntity.jwtEntity;
import static com.recipia.member.adapter.out.persistence.QMemberEntity.memberEntity;

@RequiredArgsConstructor
@Repository
public class JwtQueryRepository {

    private final JPAQueryFactory jpaQueryFactory;

    /**
     * [DELETE] member email에 해당하는 회원의 JWT를 삭제한다.
     */
    public Long deleteByMemberEmail(String email) {

        // 먼저 MemberEntity에서 member_id를 조회한다.
        Long memberId = jpaQueryFactory
                .select(memberEntity.id)
                .from(memberEntity)
                .where(memberEntity.email.eq(email))
                .fetchOne();

        // 조회한 member_id를 사용하여 JwtEntity에서 해당 JWT를 삭제한다.
        return jpaQueryFactory
                .delete(jwtEntity)
                .where(jwtEntity.memberId.eq(memberId))
                .execute();
    }
}
