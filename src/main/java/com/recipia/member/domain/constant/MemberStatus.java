package com.recipia.member.domain.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum MemberStatus {
    ACTIVE,         // 정상
    DORMANT,        // 휴면
    DEACTIVATED     // 탈퇴
}
