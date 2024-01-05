package com.recipia.member.application.port.out.port;

import com.querydsl.core.group.GroupBy;
import com.recipia.member.adapter.in.web.dto.request.SignUpRequestDto;
import com.recipia.member.domain.Member;

import java.util.Optional;

public interface SignUpPort {
    boolean isEmailAvailable(String email);
    boolean isTelNoAvailable(String telNo);
    Long signUpMember(Member member);
    Optional<SignUpRequestDto> findById(String email);
    SignUpRequestDto saveUser(String email, String password, String nickname, String introduction);
}
