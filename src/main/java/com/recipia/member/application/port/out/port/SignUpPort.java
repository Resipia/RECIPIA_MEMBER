package com.recipia.member.application.port.out.port;

import com.recipia.member.domain.Member;

public interface SignUpPort {
    boolean isEmailAvailable(String email);
    boolean isTelNoAvailable(String telNo);
    Long signUpMember(Member member);
}
