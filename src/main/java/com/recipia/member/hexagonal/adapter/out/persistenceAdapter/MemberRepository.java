package com.recipia.member.hexagonal.adapter.out.persistenceAdapter;

import com.recipia.member.hexagonal.adapter.out.persistence.MemberEntity;
import com.recipia.member.hexagonal.adapter.out.persistence.constant.MemberStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<MemberEntity, Long> {

    Optional<MemberEntity> findMemberByUsername(String username);
    Optional<MemberEntity> findMemberByUsernameAndStatus(String username, MemberStatus status);
}