package com.recipia.member.application.service;

import com.recipia.member.application.port.in.SignUpUseCase;
import com.recipia.member.application.port.out.port.SignUpPort;
import com.recipia.member.common.exception.ErrorCode;
import com.recipia.member.common.exception.MemberApplicationException;
import com.recipia.member.domain.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class SignUpService implements SignUpUseCase {

    private final SignUpPort signUpPort;

    @Override
    public Long signUp(Member member) {
        // 비밀번호 형태 검증
        if (!Member.isValidPassword(member.getPassword())) {
            throw new MemberApplicationException(ErrorCode.BAD_REQUEST);
        }

        // 비밀번호 암호화
        member.passwordEncoder();

        return signUpPort.signUpMember(member);
    }



}
