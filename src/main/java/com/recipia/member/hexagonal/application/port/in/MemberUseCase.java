package com.recipia.member.hexagonal.application.port.in;

public interface MemberUseCase {

    SignIn login(String username, String password);
}
