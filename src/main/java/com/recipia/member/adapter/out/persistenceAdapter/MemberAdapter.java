package com.recipia.member.adapter.out.persistenceAdapter;

import com.recipia.member.adapter.out.persistence.MemberEntity;
import com.recipia.member.adapter.out.persistence.constant.MemberStatus;
import com.recipia.member.application.port.out.port.MemberPort;
import com.recipia.member.common.exception.ErrorCode;
import com.recipia.member.common.exception.MemberApplicationException;
import com.recipia.member.domain.Member;
import com.recipia.member.domain.converter.MemberConverter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@RequiredArgsConstructor
@Component
public class MemberAdapter implements MemberPort {

    private final MemberRepository memberRepository;
    private final MemberConverter converter;

    @Override
    public Member findMemberById(Long memberId) {
        MemberEntity memberEntityOptional = memberRepository.findMemberById(memberId).orElseThrow(() -> new MemberApplicationException(ErrorCode.USER_NOT_FOUND));
        return converter.entityToDomain(memberEntityOptional);
    }

    @Override
    public Member findMemberByIdAndStatus(Long memberId, MemberStatus status) {
        MemberEntity memberEntityOptional = memberRepository.findMemberByIdAndStatus(memberId, MemberStatus.ACTIVE).orElseThrow(() -> new MemberApplicationException(ErrorCode.USER_NOT_FOUND));
        return converter.entityToDomain(memberEntityOptional);
    }

    /**
     * 회원가입에서도 사용되기 때문에 일부러 어기서 exception 발생시키지 않음
     * 회원가입 할때 사용자가 입력한 이메일이 db에 존재하지 않으면 true를 반환해야하기 때문
     */
    @Override
    public Optional<Member> findMemberByEmail(String email) {
        Optional<MemberEntity> memberEntityOptional = memberRepository.findMemberByEmail(email);
        return memberEntityOptional.map(converter::entityToDomain);
    }

    /**
     * 공통 클래스인 TokenValidator 에서 반환받은 member이 null인지 아닌지에 따라 boolean 값 반환하는데 사용되기때문에
     * 여기서 memberEntity 없을때 exception 던지지 않음
     * @param email
     * @param status
     * @return
     */
    @Override
    public Optional<Member> findMemberByEmailAndStatus(String email, MemberStatus status) {
        Optional<MemberEntity> memberEntityOptional = memberRepository.findMemberByEmailAndStatus(email, status);
        return memberEntityOptional.map(converter::entityToDomain);

    }
}
