package com.recipia.member.adapter.out.persistenceAdapter;

import com.recipia.member.adapter.out.persistence.MemberEntity;
import com.recipia.member.adapter.out.persistence.constant.MemberStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<MemberEntity, Long> {

    Optional<MemberEntity> findMemberById(Long memberId);
    Optional<MemberEntity> findMemberByIdAndStatus(Long memberId, MemberStatus status);

    Optional<MemberEntity> findMemberByEmail(String email);
    Optional<MemberEntity> findMemberByEmailAndStatus(String email, MemberStatus status);
    boolean existsByTelNoAndStatusNot(String telNo, MemberStatus excludedStatus);
    boolean existsByEmailAndStatusNot(String email, MemberStatus excludedStatus);
    boolean existsByFullNameAndTelNoAndEmailAndStatusNot(String fullName, String telNo, String email, MemberStatus excludedStatus);
    Optional<MemberEntity> findMemberByFullNameAndTelNo(String fullName, String telNo);

    boolean existsByNicknameAndStatusNot(String nickname, MemberStatus memberStatus);
}