package com.recipia.member.hexagonal.application.port.out.port;

import com.recipia.member.hexagonal.domain.Authentication;

public interface MemberPersistencePort {
    Authentication findMemberByUsername(String username);
}
