package com.recipia.member.hexagonal.application.port.out.port;

import com.recipia.member.hexagonal.adapter.out.persistence.constant.MemberStatus;
import com.recipia.member.hexagonal.domain.Member;

import java.util.Optional;

public interface MemberPort {

    Optional<Member> findMemberById(Long memberId);
    Optional<Member> findMemberByEmail(String email);
    Optional<Member> findMemberByEmailAndStatus(String email, MemberStatus status);

}
