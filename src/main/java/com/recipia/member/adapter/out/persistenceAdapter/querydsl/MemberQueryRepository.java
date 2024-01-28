package com.recipia.member.adapter.out.persistenceAdapter.querydsl;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.recipia.member.adapter.out.persistence.MemberEntity;
import com.recipia.member.adapter.out.persistence.constant.MemberStatus;
import com.recipia.member.domain.MyPage;
import com.recipia.member.domain.TempPassword;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

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
                .set(memberEntity.updateDateTime, LocalDateTime.now())
                .where(memberEntity.id.eq(memberId))
                .execute();
    }

    /**
     * [DELETE] memberId 해당하는 멤버 파일을 soft delete 처리한다.
     */
    public Long softDeleteMemberFile(MyPage myPage) {
        return jpaQueryFactory
                .update(memberFileEntity)
                .set(memberFileEntity.delYn, "Y")
                .set(memberFileEntity.updateDateTime, LocalDateTime.now())
                .where(memberFileEntity.memberEntity.id.eq(myPage.getMemberId()))
                .execute();
    }

    public Optional<MemberEntity> findMemberByEmailAndStatus(String email, MemberStatus status) {
        return Optional.ofNullable(jpaQueryFactory
                .selectFrom(memberEntity)
                .where(memberEntity.email.eq(email), memberEntity.status.eq(status))
                .fetchOne());
    }

    /**
     * [READ] memberIdList에 해당하는 회원중에서 status가 ACTIVE인 회원의 갯수를 반환한다.
     */
    public Long findAllMemberByIdAndStatus(List<Long> memberIdList) {
        return jpaQueryFactory
                .select(memberEntity.id.count())
                .from(memberEntity)
                .where(memberEntity.id.in(memberIdList), memberEntity.status.eq(MemberStatus.ACTIVE))
                .fetchOne();
    }

    /**
     * [UPDATE] email에 해당하는 회원의 비밀번호를 수정한다.
     * 수정된 row의 갯수를 반환한다.
     */
    public Long updatePassword(String email, String encryptedTempPassword) {
        return jpaQueryFactory
                .update(memberEntity)
                .set(memberEntity.password, encryptedTempPassword)
                .set(memberEntity.updateDateTime, LocalDateTime.now())
                .where(memberEntity.email.eq(email))
                .execute();
    }
}
