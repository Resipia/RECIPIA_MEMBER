package com.recipia.member.adapter.out.persistenceAdapter;

import com.recipia.member.adapter.out.persistence.MemberFileEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface MemberFileRepository extends JpaRepository<MemberFileEntity, Long> {
    Optional<MemberFileEntity> findByMemberEntity_Id(Long memberId);

}
