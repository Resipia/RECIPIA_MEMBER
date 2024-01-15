package com.recipia.member.adapter.out.persistenceAdapter.querydsl;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.recipia.member.adapter.out.persistence.constant.MemberStatus;
import com.recipia.member.domain.MyPage;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import static com.recipia.member.adapter.out.persistence.QMemberEntity.memberEntity;
import static com.recipia.member.adapter.out.persistence.QMemberFileEntity.memberFileEntity;

@RequiredArgsConstructor
@Repository
public class MemberQueryRepository {

    private final JPAQueryFactory jpaQueryFactory;

    /**
     * [UPDATE] memberId에 해당하는 멤버를 탈퇴처리한다.
     */
    public Long deactivateMemberByMemberId(Long memberId) {
        return jpaQueryFactory
                .update(memberEntity)
                .set(memberEntity.status, MemberStatus.DEACTIVATED)
                .where(memberEntity.id.eq(memberId))
                .execute();
    }

    /**
     * [DELETE] memberId, fileOrder에 해당하는 멤버 파일을 soft delete 처리한다.
     */
    public Long softDeleteMemberFile(MyPage myPage) {
        return jpaQueryFactory
                .update(memberFileEntity)
                .set(memberFileEntity.delYn, "Y")
                .where(memberFileEntity.memberEntity.id.eq(myPage.getMemberId()), memberFileEntity.fileOrder.eq(myPage.getDeleteFileOrder()))
                .execute();
    }
}
