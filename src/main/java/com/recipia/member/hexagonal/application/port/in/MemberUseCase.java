package com.recipia.member.hexagonal.application.port.in;

import com.recipia.member.hexagonal.domain.SignIn;

public interface MemberUseCase {

    SignIn login(String username, String password);
}
