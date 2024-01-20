package com.recipia.member.application.service;

import com.recipia.member.application.port.in.MemberManagementUseCase;
import com.recipia.member.application.port.out.port.MemberPort;
import com.recipia.member.application.port.out.port.SignUpPort;
import com.recipia.member.common.exception.ErrorCode;
import com.recipia.member.common.exception.MemberApplicationException;
import com.recipia.member.domain.Member;
import com.recipia.member.domain.Report;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
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
    private final MemberPort memberPort;

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

    /**
     * [CREATE] 회원 신고를 접수한다.
     * 저장에 성공하면 생성된 신고 id를 반환한다.
     */
    @Transactional
    @Override
    public Long reportMember(Report report) {
        // 신고를 하는 회원과 신고를 당하는 회원 둘다 존재하는 회원인지 검증
        List<Long> memberIdList = List.of(report.getReportingMemberId(), report.getReportedMemberId());
        boolean allMemberActive = memberPort.isAllMemberActive(memberIdList);

        // 둘 중 한명이라도 ACTIVE한 회원이 아니라면 에러 발생
        if (!allMemberActive) {
            throw new MemberApplicationException(ErrorCode.NOT_ACTIVE_USER);
        }

        return memberPort.saveReport(report);
    }

    /**
     * [READ] 회원의 이메일을 반환한다.
     * member full name, telNo로 멤버의 이메일을 반환한다. 없다면 에러를 발생시킨다.
     */
    @Override
    public String findEmail(Member domain) {
        return memberPort.findEmail(domain);
    }


}
