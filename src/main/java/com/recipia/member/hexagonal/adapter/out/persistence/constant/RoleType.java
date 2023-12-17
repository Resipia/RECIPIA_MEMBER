package com.recipia.member.hexagonal.adapter.out.persistence.constant;


import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum RoleType {
    ADMIN,  // 관리자
    MEMBER  // 일반 사용자
}
