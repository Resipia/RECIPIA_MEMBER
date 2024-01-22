package com.recipia.member.application.port.out.port;

import com.recipia.member.domain.Member;

public interface SignUpPort {
    Long signUpMember(Member member);
}
