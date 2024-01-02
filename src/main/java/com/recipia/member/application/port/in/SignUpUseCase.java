package com.recipia.member.application.port.in;

import com.recipia.member.domain.Member;

public interface SignUpUseCase {
    Long signUp(Member member);
}
