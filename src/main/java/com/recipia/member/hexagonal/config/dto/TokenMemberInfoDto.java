package com.recipia.member.hexagonal.config.dto;

import com.recipia.member.hexagonal.adapter.out.persistence.MemberEntity;
import com.recipia.member.hexagonal.adapter.out.persistence.constant.RoleType;

// todo: password 필요없으면 제거해야함
public record TokenMemberInfoDto(
        Long id,
        String username,
        String password,
        String nickname,
        RoleType roleType
) {

    /**
     * 전체 생성자 factory method 선언
     */
    public static TokenMemberInfoDto of(Long id, String username, String password, String nickname, RoleType roleType) {
        return new TokenMemberInfoDto(id, username, password, nickname, roleType);
    }

    public static TokenMemberInfoDto fromEntity(MemberEntity entity) {
        return TokenMemberInfoDto.of(entity.getId(), entity.getUsername(), entity.getPassword(), entity.getNickname(), entity.getRoleType());
    }

}
