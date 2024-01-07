package com.recipia.member.adapter.out.persistence.constant;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@AllArgsConstructor
public enum RoleType {
    ADMIN("ROLE_ADMIN"),  // 관리자
    MEMBER("ROLE_MEMBER");  // 일반 사용자

    private final String name;
}
