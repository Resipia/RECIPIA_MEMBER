package com.recipia.member.hexagonal.application.port.out.port;

public interface MemberPersistencePort {
    SignIn findMemberByUsername(String username);
}
