package com.recipia.member.application.service;

import com.recipia.member.application.port.in.MemberManagementUseCase;
import com.recipia.member.application.port.out.port.SignUpPort;
import com.recipia.member.common.exception.ErrorCode;
import com.recipia.member.common.exception.MemberApplicationException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@RequiredArgsConstructor
@Service
public class MemberManagementService implements MemberManagementUseCase {

    private final SignUpPort signUpPort;

    private static final String EMAIL_REGEX = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6}$";
    private static final Pattern EMAIL_PATTERN = Pattern.compile(EMAIL_REGEX);

    // db에 없는 이메일인지 확인
    @Override
    public boolean isEmailAvailable(String email) {

        // 이메일 형식이 유효한지 확인
        Matcher matcher = EMAIL_PATTERN.matcher(email);
        if (matcher.matches()) {
            // 유효한 형식이면 DB에서 검색
            return signUpPort.isEmailAvailable(email);
        } else {
            throw new MemberApplicationException(ErrorCode.INVALID_EMAIL_FORMAT);
        }
    }

    // db에 없는 휴대폰 번호인지 확인
    @Override
    public boolean isTelNoAvailable(String telNo) {
        return signUpPort.isTelNoAvailable(telNo);
    }


}
