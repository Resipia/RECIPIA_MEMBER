package com.recipia.member.hexagonal.adapter.out.persistence.entity.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum MemberStatus {
    ACTIVE,         // 정상
    DORMANT,        // 휴면
    DEACTIVATED     // 탈퇴
}
