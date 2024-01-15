package com.recipia.member.adapter.out.persistence.constant;


import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 사용자 권한에 사용되는 ENUM 객체
 */
@Getter
@AllArgsConstructor
public enum RoleType {
    ADMIN("ROLE_ADMIN"),  // 관리자
    MEMBER("ROLE_MEMBER");  // 일반 사용자

    private final String name;
}
