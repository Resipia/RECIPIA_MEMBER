package com.recipia.member.adapter.out.persistence.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 사용자 상태 관리에 사용되는 ENUM 객체
 */
@Getter
@AllArgsConstructor
public enum MemberStatus {
    ACTIVE,         // 정상
    DORMANT,        // 휴면
    DEACTIVATED     // 탈퇴
}
