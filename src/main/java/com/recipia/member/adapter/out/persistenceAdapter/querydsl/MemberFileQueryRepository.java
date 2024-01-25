package com.recipia.member.adapter.out.persistenceAdapter.querydsl;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import static com.recipia.member.adapter.out.persistence.QMemberFileEntity.memberFileEntity;

@RequiredArgsConstructor
@Repository
public class MemberFileQueryRepository {
    private final JPAQueryFactory jpaQueryFactory;

    /**
     * memberId에 해당하는 프로필 이미지를 soft delete 처리한다.
     */
    public Long softDeleteProfileImageByMemberId(Long memberId) {
        return jpaQueryFactory
                .update(memberFileEntity)
                .set(memberFileEntity.delYn, "Y")
                .where(memberFileEntity.memberEntity.id.eq(memberId))
                .execute();
    }

}
