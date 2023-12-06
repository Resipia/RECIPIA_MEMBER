package com.recipia.member.hexagonal.application.port.in;

import com.recipia.member.hexagonal.domain.Authentication;

public interface MemberUseCase {

    Authentication login(String username, String password);
}
