package com.recipia.member.adapter.out.persistenceAdapter;

import com.recipia.member.adapter.out.persistence.MemberEntity;
import com.recipia.member.application.port.out.port.SignUpPort;
import com.recipia.member.domain.Member;
import com.recipia.member.domain.converter.MemberConverter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class SignUpAdapter implements SignUpPort {

    private final MemberRepository memberRepository;
    private final MemberConverter converter;

    /**
     * [CREATE] 멤버 저장
     * 저장에 성공하면 생성된 member pk를 반환한다.
     */
    @Override
    public Long signUpMember(Member member) {
        MemberEntity memberEntity = converter.domainToEntity(member);
        memberRepository.save(memberEntity);
        return memberEntity.getId();
    }

}
