package com.recipia.member.adapter.out.persistenceAdapter;

import com.recipia.member.adapter.out.persistence.MemberEntity;
import com.recipia.member.adapter.out.persistence.MemberFileEntity;
import com.recipia.member.adapter.out.persistence.ReportEntity;
import com.recipia.member.adapter.out.persistence.constant.MemberStatus;
import com.recipia.member.adapter.out.persistenceAdapter.querydsl.MemberFileQueryRepository;
import com.recipia.member.adapter.out.persistenceAdapter.querydsl.MemberQueryRepository;
import com.recipia.member.application.port.out.port.MemberPort;
import com.recipia.member.common.exception.ErrorCode;
import com.recipia.member.common.exception.MemberApplicationException;
import com.recipia.member.domain.*;
import com.recipia.member.domain.converter.MemberConverter;
import com.recipia.member.domain.converter.ReportConverter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * 외부(DB)와의 연결을 관리한다.
 */
@RequiredArgsConstructor
@Component
public class MemberAdapter implements MemberPort {

    private final MemberRepository memberRepository;
    private final MemberConverter converter;
    private final MemberQueryRepository memberQueryRepository;
    private final MemberFileRepository memberFileRepository;
    private final ReportConverter reportConverter;
    private final ReportRepository reportRepository;

    private final MemberFileQueryRepository memberFileQueryRepository;

    /**
     * [READ] member id로 조회한 멤버 도메인을 반환한다.
     * (예외) 존재하는 멤버가 없으면 에러를 반환한다.
     */
    @Override
    public Member findMemberById(Long memberId) {
        MemberEntity memberEntityOptional = memberRepository.findMemberById(memberId).orElseThrow(() -> new MemberApplicationException(ErrorCode.USER_NOT_FOUND));
        return converter.entityToDomain(memberEntityOptional);
    }

    /**
     * [READ] member id, status로 조회한 멤버 도메인을 반환한다.
     * (예외) 존재하는 멤버가 없으면 에러를 반환한다.
     */
    @Override
    public Member findMemberByIdAndStatus(Long memberId, MemberStatus status) {
        MemberEntity memberEntityOptional = memberRepository.findMemberByIdAndStatus(memberId, MemberStatus.ACTIVE).orElseThrow(() -> new MemberApplicationException(ErrorCode.USER_NOT_FOUND));
        return converter.entityToDomain(memberEntityOptional);
    }

    /**
     * [READ] email로 멤버를 조회한다. 없으면 Optional.empty를 반환한다.
     * 회원가입에서도 사용되기 때문에 반환값이 없어도 여기서 에러를 발생시키지 않는다.
     * 왜냐하면 회원가입 할때 사용자가 입력한 이메일이 db에 존재하지 않으면 true를 반환해야하기 때문이다.
     */
    @Override
    public Optional<Member> findMemberByEmail(String email) {
        Optional<MemberEntity> memberEntityOptional = memberRepository.findMemberByEmail(email);
        return memberEntityOptional.map(converter::entityToDomain);
    }

    /**
     * [READ] email, status로 멤버를 조회한다. 없으면 Optional.empty를 반환한다.
     * 공통 클래스인 TokenValidator 에서 반환받은 member 도메인이 null인지 아닌지에 따라 boolean 값 반환하는데 사용되기때문에
     * 여기서 memberEntity 없을때 에러를 발생시키지 않는다.
     */
    @Override
    public Optional<Member> findMemberByEmailAndStatus(String email, MemberStatus status) {

//        Optional<MemberEntity> memberEntityOptional = memberRepository.findMemberByEmailAndStatus(email, status);
        Optional<MemberEntity> memberEntityOptional = memberQueryRepository.findMemberByEmailAndStatus(email, status);
        return memberEntityOptional.map(converter::entityToDomain);
    }

    /**
     * [UPDATE] memberId로 사용자를 탈퇴처리 한다.
     */
    @Override
    public Long deactivateMember(Long memberId) {
        return memberQueryRepository.deactivateMemberByMemberId(memberId);
    }

    /**
     * [CREATE] 사용자 프로필 이미지 저장을 담당한다.
     * 저장한 후에 생성된 pk값을 반환한다.
     */
    @Override
    public Long saveMemberFile(MemberFile memberFile) {
        MemberFileEntity memberFileEntity = converter.domainToEntity(memberFile);
        memberFileEntity = memberFileRepository.save(memberFileEntity);
        return memberFileEntity.getId();
    }

    /**
     * [UPDATE] 사용자 프로필 이미지 삭제처리(del_yn = 'Y')를 담당한다.
     */
    @Override
    public Long softDeleteProfileImage(MyPage myPage) {
        return memberQueryRepository.softDeleteMemberFile(myPage);
    }


    /**
     * [READ] memberIdList로 들어온 회원이 전부 ACTIVE 상태인지 검증한다.
     * 회원이 전부 ACTIVE 상태면 true, 한명이라도 ACTIVE 상태가 아니라면 false를 반환한다.
     */
    @Override
    public boolean isAllMemberActive(List<Long> memberIdList) {
        Long memberCount = memberQueryRepository.findAllMemberByIdAndStatus(memberIdList);
        return memberIdList.size() == memberCount;
    }

    /**
     * [CREATE] 회원 신고를 저장한다.
     * 저장에 성공하면 생성된 report id를 반환한다.
     */
    @Override
    public Long saveReport(Report report) {
        ReportEntity reportEntity = reportConverter.domainToEntity(report);
        return reportRepository.save(reportEntity).getId();
    }

    /**
     * [READ] 회원 이메일을 반환한다.
     * 존재하는 회원을 받으면 email을 반환하고, 없는 회원이면 에러를 발생시킨다.
     */
    @Override
    public String findEmail(Member domain) {
        MemberEntity memberEntity = memberRepository.findMemberByFullNameAndTelNo(domain.getFullName(), domain.getTelNo()).orElseThrow(
                () -> new MemberApplicationException(ErrorCode.USER_NOT_FOUND)
        );
        return memberEntity.getEmail();
    }

    /**
     * [READ] 탈퇴 회원이 아닌 회원중에서 이메일이 존재하는지 검증한다.
     * 회원이 존재하면 true, 없으면 false를 반환한다.
     */
    @Override
    public boolean existsByEmailNotInDeactive(String email) {
        return memberRepository.existsByEmailAndStatusNot(email, MemberStatus.DEACTIVATED);
    }

    /**
     * [UPDATE] 임시로 발급된 비밀번호로 회원 비밀번호 업데이트한다.
     * 업데이트된 row의 갯수를 반환한다.
     */
    @Transactional
    @Override
    public Long updatePasswordByEmail(String email, String encryptedTempPassword) {
        return memberQueryRepository.updatePasswordByEamil(email, encryptedTempPassword);
    }

    /**
     * [DELETE] memberId에 해당하는 프로필 이미지를 삭제처리한다.
     */
    @Override
    public Long softDeleteProfileImageByMemberId(Long memberId) {
        return memberFileQueryRepository.softDeleteProfileImageByMemberId(memberId);
    }

    /**
     * [READ] memberId에 해당하는 프로필 이미지 저장 경로를 가져온다.
     */
    @Override
    public String getFileFullPath(Long memberId) {
        return memberFileQueryRepository.getFileFullPath(memberId);
    }

    /**
     * [UPDATE] memberId에 해당하는 회원의 비밀번호를 수정한다.
     */
    @Override
    public Long updatePasswordByMemberId(Long memberId, String password) {
        return memberQueryRepository.updatepasswordByMemberId(memberId, password);
    }


}
