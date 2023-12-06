package com.recipia.member.hexagonal.adapter.out.persistenceAdapter;

import com.recipia.member.hexagonal.application.port.out.port.MemberPersistencePort;
import com.recipia.member.hexagonal.domain.Authentication;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class MemberPersistenceAdapter implements MemberPersistencePort {

    private final MemberRepository memberRepository;

    @Override
    public Authentication findMemberByUsername(String username) {
        return null;
    }
}
