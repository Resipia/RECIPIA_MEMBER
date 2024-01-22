package com.recipia.member.domain.converter;

import com.recipia.member.adapter.in.web.dto.request.FindEmailRequestDto;
import com.recipia.member.adapter.in.web.dto.request.SignUpRequestDto;
import com.recipia.member.adapter.out.persistence.MemberEntity;
import com.recipia.member.adapter.out.persistence.MemberFileEntity;
import com.recipia.member.adapter.out.persistence.constant.MemberStatus;
import com.recipia.member.adapter.out.persistence.constant.RoleType;
import com.recipia.member.domain.Member;
import com.recipia.member.domain.MemberFile;
import org.springframework.stereotype.Component;

/**
 * dto, entity와 domain을 변환해주는 로직과
 * domain을 entity로 변환해주는 로직을 담당
 */
@Component
public class MemberConverter {

    /**
     * MemberEntity를 받아서 Member Domain을 반환 (프로필 이미지 취급X)
     *
     * @param entity MemberEntity
     * @return MemberDoamin
     */
    public Member entityToDomain(MemberEntity entity) {
        return Member.of(
                entity.getId(),
                entity.getEmail(),
                entity.getPassword(),
                entity.getFullName(),
                entity.getNickname(),
                entity.getStatus(),
                entity.getIntroduction(),
                entity.getTelNo(),
                entity.getAddress1(),
                entity.getAddress2(),
                entity.getRoleType(),
                null,
                null,
                entity.getBirth(),
                entity.getGender()
        );
    }

    /**
     * MemberDomain을 받아서 MemberEntity를 반환
     *
     * @param member MemberDomain
     * @return MemberEntity
     */
    public MemberEntity domainToEntity(Member member) {
        return MemberEntity.of(
                member.getEmail(),
                member.getPassword(),
                member.getFullName(),
                member.getNickname(),
                member.getStatus(),
                member.getIntroduction(),
                member.getTelNo(),
                member.getAddress1(),
                member.getAddress2(),
                member.getRoleType(),
                member.getBirth(),
                member.getGender()
        );
    }

    public MemberFileEntity domainToEntity(MemberFile domain) {
        MemberEntity memberEntity = MemberEntity.of(domain.getMember().getId());
        return MemberFileEntity.of(
                memberEntity,
                domain.getFileOrder(),
                domain.getStoredFilePath(),
                domain.getObjectUrl(),
                domain.getOriginFileNm(),
                domain.getStoredFileNm(),
                domain.getFileExtension(),
                domain.getFileSize(),
                domain.getDelYn()
        );
    }

    public Member findEmailDtoToDomain(FindEmailRequestDto dto) {
        return Member.builder().fullName(dto.getFullName()).telNo(dto.getTelNo()).build();
    }


    public Member requestDtoToDomain(SignUpRequestDto req) {
        return Member.of(
                null, req.getEmail(), req.getPassword(), req.getFullName(), req.getNickname(), MemberStatus.ACTIVE, req.getIntroduction(), req.getTelNo(), req.getAddress1(), req.getAddress2(), RoleType.MEMBER, req.getIsPersonalInfoConsent(), req.getIsDataRetentionConsent(), req.getBirth(), req.getGender());
    }


}
