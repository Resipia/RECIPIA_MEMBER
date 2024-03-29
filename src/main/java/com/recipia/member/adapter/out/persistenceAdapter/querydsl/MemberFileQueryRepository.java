package com.recipia.member.adapter.out.persistenceAdapter.querydsl;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

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
                .set(memberFileEntity.updateDateTime, LocalDateTime.now())
                .where(memberFileEntity.memberEntity.id.eq(memberId))
                .execute();
    }

    /**
     * [READ] memberId에 해당하는 파일의 저장경로를 반환한다.
     */
    public String getFileFullPath(Long memberId) {
        return jpaQueryFactory
                .select(memberFileEntity.storedFilePath)
                .from(memberFileEntity)
                .where(memberFileEntity.memberEntity.id.eq(memberId), memberFileEntity.delYn.eq("N"))
                .fetchOne();
    }
}
