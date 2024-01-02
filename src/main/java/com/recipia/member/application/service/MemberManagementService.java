package com.recipia.member.application.service;

import com.recipia.member.application.port.in.MemberManagementUseCase;
import com.recipia.member.application.port.out.port.SignUpPort;
import com.recipia.member.common.exception.ErrorCode;
import com.recipia.member.common.exception.MemberApplicationException;
import com.recipia.member.domain.MemberManagement;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class MemberManagementService implements MemberManagementUseCase {

    private final SignUpPort signUpPort;

    // db에 없는 이메일인지 확인
    @Override
    public boolean isEmailAvailable(String email) {
       return Optional.of(email)
               .filter(MemberManagement::isValidEmail)      // 이메일 형식이 유효한지 확인
               .map(signUpPort::isEmailAvailable)   // db에 이미 있는 이메일인지 검증
               .orElseThrow(() -> new MemberApplicationException(ErrorCode.INVALID_EMAIL_FORMAT));
    }

    // db에 없는 휴대폰 번호인지 확인
    @Override
    public boolean isTelNoAvailable(String telNo) {
        return signUpPort.isTelNoAvailable(telNo);
    }


}
