package com.recipia.member.domain.converter;

import com.recipia.member.adapter.in.web.dto.request.SignUpRequestDto;
import com.recipia.member.adapter.out.persistence.MemberEntity;
import com.recipia.member.adapter.out.persistence.constant.MemberStatus;
import com.recipia.member.adapter.out.persistence.constant.RoleType;
import com.recipia.member.domain.Member;
import org.springframework.stereotype.Component;

/**
 * dto, entity와 domain을 변환해주는 로직과
 * domain을 entity로 변환해주는 로직을 담당
 */
@Component
public class MemberConverter {

    /**
     * MemberEntity를 받아서 Member Domain을 반환
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
                entity.getRoleType()
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
                member.getRoleType()
        );
    }

    public Member requestDtoToDomain(SignUpRequestDto req) {
        return Member.of(
                null, req.getEmail(), req.getPassword(), req.getFullName(), req.getNickname(), MemberStatus.ACTIVE, req.getIntroduction(), req.getTelNo(), req.getAddress1(), req.getAddress2(), RoleType.MEMBER
        );
    }


}
