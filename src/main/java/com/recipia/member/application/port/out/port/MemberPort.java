package com.recipia.member.application.port.out.port;

import com.recipia.member.adapter.out.persistence.constant.MemberStatus;
import com.recipia.member.domain.*;

import java.util.List;
import java.util.Optional;

public interface MemberPort {

    Member findMemberById(Long memberId);
    Member findMemberByIdAndStatus(Long memberId, MemberStatus status);
    Optional<Member> findMemberByEmail(String email);
    Optional<Member> findMemberByEmailAndStatus(String email, MemberStatus status);
    Long deactivateMember(Long memberId);
    Long saveMemberFile(MemberFile memberFile);
    Long softDeleteProfileImage(MyPage myPage);
    Integer findMaxFileOrder(Long memberId);
    boolean isAllMemberActive(List<Long> memberIdList);
    Long saveReport(Report report);

    String findEmail(Member domain);
    boolean existsByEmailNotInDeactive(String email);
    Long updatePassword(String email, String encryptedTempPassword);
}
