package com.recipia.member.repository;

import com.recipia.member.domain.Member;
import com.recipia.member.domain.constant.MemberStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {

    Optional<Member> findMemberByUsername(String username);
    Optional<Member> findMemberByUsernameAndStatus(String username, MemberStatus status);
}