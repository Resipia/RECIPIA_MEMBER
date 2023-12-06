package com.recipia.member.hexagonal.application.service;

import com.recipia.member.hexagonal.application.port.in.MemberUseCase;
import com.recipia.member.hexagonal.application.port.out.port.MemberPersistencePort;
import com.recipia.member.hexagonal.domain.Authentication;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class MemberService implements MemberUseCase {

    private final MemberPersistencePort memberPersistencePort;

    @Override
    public Authentication login(String username, String password) {
        Authentication authentication = memberPersistencePort.findMemberByUsername(username);

        return null;
    }
}
