package com.recipia.member.application.port.in;

import com.recipia.member.domain.Member;
import com.recipia.member.domain.Report;

public interface MemberManagementUseCase {
    boolean isEmailAvailable(String email);
    boolean isTelNoAvailable(String telNo);
    Long reportMember(Report report);
    String findEmail(Member member);
}
