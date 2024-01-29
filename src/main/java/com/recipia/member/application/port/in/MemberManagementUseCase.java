package com.recipia.member.application.port.in;

import com.recipia.member.domain.ChangePassword;
import com.recipia.member.domain.Member;
import com.recipia.member.domain.Report;
import com.recipia.member.domain.TempPassword;

public interface MemberManagementUseCase {
    boolean isEmailAvailable(String email);
    boolean isTelNoAvailable(String telNo);
    Long reportMember(Report report);
    String findEmail(Member member);
    void sendTempPassword(TempPassword tempPassword);
    boolean isNicknameAvailable(String nickname);
    String getProfilePreUrl(Long memberId);
    Long changePassword(ChangePassword changePassword);
}
