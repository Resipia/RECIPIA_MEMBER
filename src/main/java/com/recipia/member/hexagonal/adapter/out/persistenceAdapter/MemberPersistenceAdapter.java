package com.recipia.member.hexagonal.adapter.out.persistenceAdapter;

import com.recipia.member.hexagonal.application.port.out.port.MemberPersistencePort;
import com.recipia.member.hexagonal.domain.SignIn;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class MemberPersistenceAdapter implements MemberPersistencePort {

    private final MemberRepository memberRepository;

    @Override
    public SignIn findMemberByUsername(String username) {
        return null;
    }
}
