package com.recipia.member.adapter.out.persistenceAdapter;

import com.recipia.member.adapter.out.persistence.MemberEntity;
import com.recipia.member.adapter.out.persistence.MemberFileEntity;
import com.recipia.member.adapter.out.persistence.constant.MemberStatus;
import com.recipia.member.adapter.out.persistenceAdapter.querydsl.MemberQueryRepository;
import com.recipia.member.application.port.out.port.MemberPort;
import com.recipia.member.common.exception.ErrorCode;
import com.recipia.member.common.exception.MemberApplicationException;
import com.recipia.member.domain.Member;
import com.recipia.member.domain.MemberFile;
import com.recipia.member.domain.converter.MemberConverter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

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
        Optional<MemberEntity> memberEntityOptional = memberRepository.findMemberByEmailAndStatus(email, status);
        return memberEntityOptional.map(converter::entityToDomain);

    }

    /**
     * [UPDATE] memberId로 사용자를 탈퇴처리 한다.
     */
    @Override
    public Long deactivateMember(Long memberId) {
        Long updatedCount = memberQueryRepository.deactivateMemberByMemberId(memberId);
        return updatedCount;
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
    public Long softDeleteProfileImage(Long memberId) {
        Long updatedCount = memberQueryRepository.softDeleteMemberFile(memberId);
        return updatedCount;
    }


}
