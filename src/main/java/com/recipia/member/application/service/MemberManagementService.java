package com.recipia.member.application.service;

import com.recipia.member.application.port.in.MemberManagementUseCase;
import com.recipia.member.application.port.out.port.SignUpPort;
import com.recipia.member.common.exception.ErrorCode;
import com.recipia.member.common.exception.MemberApplicationException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 회원 관리 서비스 클래스
 */
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class MemberManagementService implements MemberManagementUseCase {

    private final SignUpPort signUpPort;

    private static final String EMAIL_REGEX = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6}$";
    private static final Pattern EMAIL_PATTERN = Pattern.compile(EMAIL_REGEX);

    /**
     * [READ] email 중복 체크
     * DB에 없는 email이면 true, DB에 이미 있는 email이면 false 반환
     */
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

    /**
     * [READ] telNo 중복 체크
     * DB에 없는 telNo면 true, DB에 이미 있는 telNo면 false 반환
     */
    @Override
    public boolean isTelNoAvailable(String telNo) {
        return signUpPort.isTelNoAvailable(telNo);
    }


}
