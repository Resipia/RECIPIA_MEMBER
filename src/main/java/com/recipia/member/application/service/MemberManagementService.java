package com.recipia.member.application.service;

import com.recipia.member.application.port.in.MemberManagementUseCase;
import com.recipia.member.application.port.out.port.JwtPort;
import com.recipia.member.application.port.out.port.MemberManagementPort;
import com.recipia.member.application.port.out.port.MemberPort;
import com.recipia.member.common.exception.ErrorCode;
import com.recipia.member.common.exception.MemberApplicationException;
import com.recipia.member.common.utils.TempPasswordUtil;
import com.recipia.member.domain.ChangePassword;
import com.recipia.member.domain.Member;
import com.recipia.member.domain.Report;
import com.recipia.member.domain.TempPassword;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
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

    private final MemberPort memberPort;
    private final JwtPort jwtPort;
    private final MailService mailService;
    private final PasswordEncoder passwordEncoder;
    private final TempPasswordUtil tempPasswordUtil;
    private final MemberManagementPort memberManagementPort;
    private final ImageS3Service imageS3Service;
    private final BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();


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
            return memberManagementPort.isEmailAvailable(email);
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
        return memberManagementPort.isTelNoAvailable(telNo);
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

    /**
     * [CREATE] 임시 비밀번호를 이메일로 전송 후, 임시 비밀번호 저장한다.
     * email로 회원을 검색해서 존재하면 임시 비밀번호를 해당 이메일로 전송한다.
     * 해당 회원이 존재하지 않으면 에러를 발생시킨다.
     * (여기서 회원 상태검사를 하지 않는 이유는 휴면 상태인 회원의 이메일도 검사해야하기 때문이다.)
     *
     * 이메일 전송에 성공했으면 회원 비밀번호를 임시로 발급된 비밀번호로 업데이트해준다.
     */
    @Transactional
    @Override
    public void sendTempPassword(TempPassword tempPassword) {
        String email = tempPassword.getEmail();

        // 탈퇴계정이 아닌 회원중에서 존재하는 이메일인지 검증
        boolean isMemberExist = memberPort.isMemberNotInDeactive(tempPassword);

        // 존재하는 이메일이 아니면 에러 발생
        if (!isMemberExist) {
            throw new MemberApplicationException(ErrorCode.USER_NOT_FOUND);
        }

        String createdTempPassword = tempPasswordUtil.generateTempPassword();
        String encryptedPassword = passwordEncoder.encode(createdTempPassword);

        // 회원 이메일로 임시로 발급된 비밀번호 전송 (비동기로 진행)
        mailService.sendTemporaryPassword(email, createdTempPassword)
                .thenAcceptAsync(result -> {
                    if (result) {   // 메일 전송 성공 시
                        // 회원 비밀번호를 임시로 발급된 비밀번호를 암호화 한 데이터로 업데이트
                        memberPort.updatePasswordByEmail(email, encryptedPassword);
                        // 임시 비밀번호로 업데이트 완료했으면 계정 로그아웃 처리 (JWT 삭제까지만 진행)
                        jwtPort.deleteRefreshTokenByEmail(email);
                    } else {
                        throw new MemberApplicationException(ErrorCode.MAIL_SEND_FAILED);
                    }
                });

    }

    /**
     * [READ] nickname 중복체크
     * DB에 없는 nickname이면 true, DB에 이미 있는 nickname이면 false 반환
     */
    @Override
    public boolean isNicknameAvailable(String nickname) {
        return memberManagementPort.isNicknameAvailable(nickname);
    }

    /**
     * [READ] memberId에 해당하는 프로필 사진의 pre-signed-url을 반환한다.
     */
    @Override
    public String getProfilePreUrl(Long memberId) {
        String fileFullPath = memberPort.getFileFullPath(memberId);
        if (fileFullPath != null) {
            return imageS3Service.generatePreSignedUrl(fileFullPath, 60);
        } else {
            return null;
        }
    }

    /**
     * [UPDATE] memberId에 해당하는 회원의 비밀번호를 수정하고 업데이트된 row의 갯수를 반환한다.
     */
    @Transactional
    @Override
    public Long changePassword(ChangePassword changePassword) {

        Member member = memberPort.findMemberById(changePassword.getMemberId());

        // 기존 비밀번호가 일치하는지 검증한다.
        // matches 메서드는 첫 번째 파라미터에 평문 비밀번호를, 두 번째 파라미터로 인코딩된 비밀번호를 받아야한다.
        if (!bCryptPasswordEncoder.matches(changePassword.getOriginPassword(), member.getPassword())) {
            throw new MemberApplicationException(ErrorCode.USER_NOT_FOUND);
        }

        // 유효한 확장자인지 검증한다.
        boolean isValidPassword = member.isValidPassword(changePassword.getNewPassword());
        if (!isValidPassword) {
            throw new MemberApplicationException(ErrorCode.BAD_REQUEST);
        }
        String encodedPassword = passwordEncoder.encode(changePassword.getNewPassword());
        return memberPort.updatePasswordByMemberId(changePassword.getMemberId(), encodedPassword);
    }
}
