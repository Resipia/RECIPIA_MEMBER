package com.recipia.member.application.port.out.port;

import com.recipia.member.adapter.out.persistence.constant.MemberStatus;
import com.recipia.member.domain.Member;

import java.util.Optional;

public interface MemberPort {

    Member findMemberById(Long memberId);
    Optional<Member> findMemberByEmail(String email);
    Optional<Member> findMemberByEmailAndStatus(String email, MemberStatus status);

}
