package com.recipia.member.adapter.out.persistenceAdapter.querydsl;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.recipia.member.adapter.out.persistence.constant.MemberStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import static com.recipia.member.adapter.out.persistence.QMemberEntity.memberEntity;
import static com.recipia.member.adapter.out.persistence.QMemberFileEntity.memberFileEntity;

@RequiredArgsConstructor
@Repository
public class MemberQueryRepository {

    private final JPAQueryFactory jpaQueryFactory;

    /**
     * memberId에 해당하는 멤버를 탈퇴처리한다.
     */
    public Long deactivateMemberByMemberId(Long memberId) {
        return jpaQueryFactory
                .update(memberEntity)
                .set(memberEntity.status, MemberStatus.DEACTIVATED)
                .where(memberEntity.id.eq(memberId))
                .execute();
    }

    /**
     * memberId에 해당하는 멤버의 프로필 이미지를 soft delete 처리한다.
     */
    public Long softDeleteMemberFile(Long memberId) {
        return jpaQueryFactory
                .update(memberFileEntity)
                .set(memberFileEntity.delYn, "Y")
                .where(memberFileEntity.memberEntity.id.eq(memberId))
                .execute();
    }
}
