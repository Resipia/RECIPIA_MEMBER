package com.recipia.member.application.service;

import com.recipia.member.adapter.out.persistence.constant.ReportStatus;
import com.recipia.member.adapter.out.persistence.constant.ReportType;
import com.recipia.member.adapter.out.persistenceAdapter.MemberManagementAdapter;
import com.recipia.member.application.port.out.port.JwtPort;
import com.recipia.member.application.port.out.port.MemberPort;
import com.recipia.member.common.utils.TempPasswordUtil;
import com.recipia.member.domain.Member;
import com.recipia.member.domain.Report;
import com.recipia.member.domain.TempPassword;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.concurrent.CompletableFuture;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("[단위] 회원 관리 서비스 테스트")
class MemberManagementServiceTest {

    @InjectMocks
    private MemberManagementService sut;
    @Mock
    private MemberPort memberPort;
    @Mock
    private MemberManagementAdapter memberManagementAdapter;
    @Mock
    private TempPasswordUtil tempPasswordUtil;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private MailService mailService;
    @Mock
    private JwtPort jwtPort;


    @DisplayName("[happy] DB에 없는 이메일이 들어왔을때 true를 리턴한다.")
    @Test
    void checkEmailDuplicationTestSuccess() {
        //given
        String email = "test1@gmail.com";

        //when
        when(memberManagementAdapter.isEmailAvailable(email)).thenReturn(true);
        boolean isEmailAvailable = sut.isEmailAvailable(email);

        //then
        assertThat(isEmailAvailable).isTrue();

    }

    @DisplayName("[bad] DB에 이미 존재하는 이메일이 들어왔을때 false를 리턴한다.")
    @Test
    void checkEmailDuplicationTestFail() {
        //given
        String email = "test1@example.com";

        //when
        when(memberManagementAdapter.isEmailAvailable(email)).thenReturn(false);
        boolean isEmailAvailable = sut.isEmailAvailable(email);
        //then
        assertThat(isEmailAvailable).isFalse();
    }

    @DisplayName("[happy] DB에 없는 휴대폰 번호가 들어왔을때 true를 리턴한다.")
    @Test
    void checkTelNoDuplicationTestSuccess() {
        //given
        String telNo = "101-1111-1111";

        //when
        when(memberManagementAdapter.isTelNoAvailable(telNo)).thenReturn(true);
        boolean isTelNoAvailable = sut.isTelNoAvailable(telNo);

        //then
        assertThat(isTelNoAvailable).isTrue();

    }

    @DisplayName("[bad] DB에 이미 존재하는 휴대폰 번호가 들어왔을때 false를 리턴한다.")
    @Test
    void checkTelNoDuplicationTestFail() {
        //given
        String telNo = "01012345678";

        //when
        when(memberManagementAdapter.isTelNoAvailable(telNo)).thenReturn(false);
        boolean isTelNoAvailable = sut.isTelNoAvailable(telNo);

        //then
        assertThat(isTelNoAvailable).isFalse();
    }

    @DisplayName("[happy] 신고를 하는 회원과 신고를 당하는 회원 모두 ACTIVE 상태일때 신고 접수가 성공한다.")
    @Test
    void reportSaveSuccess() {
        // given
        Long reportedMemberId = 3L;     // 신고 당하는 회원
        Long reportingMemberId = 1L;    // 신고하는 회원
        Report report = Report.of(reportedMemberId, reportingMemberId, ReportType.SPAM, "", ReportStatus.RECEIVED);
        when(memberPort.isAllMemberActive(eq(List.of(reportingMemberId, reportedMemberId)))).thenReturn(true);
        when(sut.reportMember(report)).thenReturn(1L);

        // when
        Long savedReportId = sut.reportMember(report);
        // then
        assertThat(savedReportId).isNotNull();
        assertThat(savedReportId).isGreaterThan(0L);

    }

    @DisplayName("[happy] db에 존재하는 회원의 이메일은 반환받는다.")
    @Test
    void findEmailSuccess() {
        // given
        Member member = Member.builder().fullName("홍길동").telNo("01012345678").build();
        when(sut.findEmail(member)).thenReturn("hong1@example.com");
        // when
        String result = sut.findEmail(member);
        // then
        assertEquals(result, "hong1@example.com");
    }

    @DisplayName("[happy] db에 존재하는 회원의 이메일을 받으면 메일 전송 서비스가 호출된다.")
    @Test
    void sendTempPasswordTest() {
        // given
        String email = "hong1@example.com";
        String createdTempPassword = "tempPassword";
        TempPassword domain = TempPassword.of(email);
        when(memberPort.existsByEmailNotInDeactive(email)).thenReturn(true);
        when(tempPasswordUtil.generateTempPassword()).thenReturn(createdTempPassword);

        // CompletableFuture를 반환하도록 스터빙
        when(mailService.sendTemporaryPassword(eq(email), eq(createdTempPassword)))
                .thenReturn(CompletableFuture.completedFuture(true));

        // when
        sut.sendTempPassword(domain);

        // then
        verify(mailService).sendTemporaryPassword(eq(email), eq(createdTempPassword));
        verify(passwordEncoder).encode(anyString()); // 모든 문자열 인자에 대해 스터빙
    }

    @DisplayName("[happy] db에 존재하는 회원의 이메일을 받으면 비밀번호 수정 메서드가 호출된다.")
    @Test
    void updateEncryptedTempPassword() {
        // given
        String email = "hong1@example.com";
        String createdTempPassword = "tempPassword";
        TempPassword domain = TempPassword.of(email);
        when(memberPort.existsByEmailNotInDeactive(email)).thenReturn(true);
        when(tempPasswordUtil.generateTempPassword()).thenReturn(createdTempPassword);
        when(passwordEncoder.encode(anyString())).thenReturn("encryptedPassword");
        when(memberPort.updatePassword(email, "encryptedPassword")).thenReturn(1L);

        // CompletableFuture를 반환하도록 스터빙
        when(mailService.sendTemporaryPassword(eq(email), eq(createdTempPassword)))
                .thenReturn(CompletableFuture.completedFuture(true));

        when(memberPort.updatePassword(email, "encryptedPassword")).thenReturn(1L);

        // when
        sut.sendTempPassword(domain);

        // then
        verify(memberPort).updatePassword(email, "encryptedPassword");
    }

    @DisplayName("[happy] db에 없는 닉네임을 요청받으면 true를 반환한다.")
    @Test
    void isNicknameAvailableTrue() {
        // given
        String nickname = "hello";
        when(sut.isNicknameAvailable(nickname)).thenReturn(true);
        // when
        boolean isNicknameAvailable = sut.isNicknameAvailable(nickname);
        // then
        assertTrue(isNicknameAvailable);
    }

}