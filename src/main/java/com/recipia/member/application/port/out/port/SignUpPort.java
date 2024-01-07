package com.recipia.member.application.port.out.port;

import com.querydsl.core.group.GroupBy;
import com.recipia.member.adapter.in.web.dto.request.SignUpRequestDto;
import com.recipia.member.domain.Member;
import com.recipia.member.domain.MemberFile;

import java.util.Optional;

public interface SignUpPort {
    boolean isEmailAvailable(String email);
    boolean isTelNoAvailable(String telNo);
    Long signUpMember(Member member);
    Long saveMemberFile(MemberFile memberFile);
}
