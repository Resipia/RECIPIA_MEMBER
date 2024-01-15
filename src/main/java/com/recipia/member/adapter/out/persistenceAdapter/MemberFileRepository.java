package com.recipia.member.adapter.out.persistenceAdapter;

import com.recipia.member.adapter.out.persistence.MemberFileEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface MemberFileRepository extends JpaRepository<MemberFileEntity, Long> {
    Optional<MemberFileEntity> findByMemberEntity_Id(Long memberId);

    @Query("SELECT MAX(mfe.fileOrder) FROM MemberFileEntity mfe WHERE mfe.memberEntity.id = :memberId")
    Optional<Integer> findMaxFileOrderByMemberEntity_Id(@Param("memberId") Long memberId);
}
