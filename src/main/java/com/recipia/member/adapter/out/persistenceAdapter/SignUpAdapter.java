package com.recipia.member.adapter.out.persistenceAdapter;

import com.recipia.member.adapter.out.persistence.MemberEntity;
import com.recipia.member.adapter.out.persistence.MemberFileEntity;
import com.recipia.member.application.port.out.port.SignUpPort;
import com.recipia.member.domain.Member;
import com.recipia.member.domain.MemberFile;
import com.recipia.member.domain.converter.MemberConverter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@RequiredArgsConstructor
@Component
public class SignUpAdapter implements SignUpPort {

    private final MemberRepository memberRepository;
    private final MemberFileRepository memberFileRepository;
    private final MemberConverter converter;

    @Override
    public boolean isEmailAvailable(String email) {
        Optional<MemberEntity> member = memberRepository.findMemberByEmail(email);
        return member.isEmpty(); // 이메일이 DB에 존재하지 않으면 true 반환
    }

    @Override
    public boolean isTelNoAvailable(String telNo) {
        Optional<MemberEntity> member = memberRepository.findMemberByTelNo(telNo);
        return member.isEmpty(); // 휴대폰 번호가 DB에 존재하지 않으면 true 반환
    }

    @Override
    public Long signUpMember(Member member) {
        MemberEntity memberEntity = converter.domainToEntity(member);
        memberRepository.save(memberEntity);

        return memberEntity.getId();

    }

    @Override
    public Long saveMemberFile(MemberFile memberFile) {
        MemberFileEntity memberFileEntity = converter.domainToEntity(memberFile);
        memberFileEntity = memberFileRepository.save(memberFileEntity);
        return memberFileEntity.getId();
    }


}
