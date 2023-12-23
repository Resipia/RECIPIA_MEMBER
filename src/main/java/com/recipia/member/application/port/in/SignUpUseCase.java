package com.recipia.member.application.port.in;

import com.recipia.member.domain.Member;

public interface SignUpUseCase {
    boolean isEmailAvailable(String email);
    boolean isTelNoAvailable(String telNo);
    Long signUp(Member member);
}
