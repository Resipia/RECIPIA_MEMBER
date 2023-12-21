package com.recipia.member.application.service;

import com.recipia.member.application.port.in.SignUpUseCase;
import com.recipia.member.application.port.out.port.SignUpPort;
import com.recipia.member.domain.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class SignUpService implements SignUpUseCase {

    private final SignUpPort signUpPort;


    @Override
    public boolean isEmailAvailable(String email) {
        return signUpPort.isEmailAvailable(email);
    }

    @Override
    public boolean isTelNoAvailable(String telNo) {
        return signUpPort.isTelNoAvailable(telNo);
    }


    @Override
    public Long signUp(Member member) {
        return signUpPort.signUpMember(member);
    }



}
