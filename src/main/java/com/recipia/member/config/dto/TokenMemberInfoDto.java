package com.recipia.member.config.dto;

import com.recipia.member.adapter.out.persistence.MemberEntity;
import com.recipia.member.adapter.out.persistence.constant.MemberStatus;
import com.recipia.member.adapter.out.persistence.constant.RoleType;
import com.recipia.member.domain.Member;

// todo: password 필요없으면 제거해야함

/**
 * spring security에서 principal 끼우는 객체
 */
public record TokenMemberInfoDto(
        Long id,
        String email,
        String password,
        String nickname,
        MemberStatus memberStatus,
        RoleType roleType
) {

    /**
     * 전체 생성자 factory method 선언
     */
    public static TokenMemberInfoDto of(Long id, String email, String password, String nickname, MemberStatus memberStatus, RoleType roleType) {
        return new TokenMemberInfoDto(id, email, password, nickname, memberStatus, roleType);
    }

    public static TokenMemberInfoDto of(Long id, String email, String nickname) {
        return new TokenMemberInfoDto(id, email, null, nickname, null, null);
    }

    public static TokenMemberInfoDto fromMemberEntity(MemberEntity entity) {
        return TokenMemberInfoDto.of(entity.getId(), entity.getEmail(), entity.getPassword(), entity.getNickname(), entity.getStatus(), entity.getRoleType());
    }

    public static TokenMemberInfoDto fromDomain(Member domain) {
        return TokenMemberInfoDto.of(domain.getId(), domain.getEmail(), domain.getPassword(), domain.getNickname(), domain.getStatus(), domain.getRoleType());
    }

}
