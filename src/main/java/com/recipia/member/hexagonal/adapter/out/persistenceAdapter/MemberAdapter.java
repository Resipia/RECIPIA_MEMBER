package com.recipia.member.hexagonal.adapter.out.persistenceAdapter;

import com.recipia.member.hexagonal.adapter.out.persistence.MemberEntity;
import com.recipia.member.hexagonal.adapter.out.persistence.constant.MemberStatus;
import com.recipia.member.hexagonal.application.port.out.port.MemberPort;
import com.recipia.member.hexagonal.common.exception.ErrorCode;
import com.recipia.member.hexagonal.common.exception.MemberApplicationException;
import com.recipia.member.hexagonal.domain.Member;
import com.recipia.member.hexagonal.domain.converter.MemberConverter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@RequiredArgsConstructor
@Component
public class MemberAdapter implements MemberPort {

    private final MemberRepository memberRepository;

    @Override
    public Optional<Member> findMemberById(Long memberId) {
        Optional<MemberEntity> memberEntityOptional = memberRepository.findMemberById(memberId);
        return memberEntityOptional.map(MemberConverter::entityToDomain);
    }

    @Override
    public Optional<Member> findMemberByEmail(String email) {
        Optional<MemberEntity> memberEntityOptional = memberRepository.findMemberByEmail(email);
        return memberEntityOptional.map(MemberConverter::entityToDomain);
    }

    @Override
    public Optional<Member> findMemberByEmailAndStatus(String email, MemberStatus status) {
        Optional<MemberEntity> memberEntityOptional = memberRepository.findMemberByEmailAndStatus(email, status);
        return memberEntityOptional.map(MemberConverter::entityToDomain);

    }
}
