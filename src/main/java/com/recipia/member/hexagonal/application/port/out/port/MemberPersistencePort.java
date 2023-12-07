package com.recipia.member.hexagonal.application.port.out.port;

import com.recipia.member.hexagonal.domain.SignIn;

public interface MemberPersistencePort {
    SignIn findMemberByUsername(String username);
}
