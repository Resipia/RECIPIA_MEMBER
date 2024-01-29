package com.recipia.member.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 비밀번호 수정 도메인 객체
 */
@Getter
@NoArgsConstructor
public class ChangePassword {
    private Long memberId;
    private String originPassword;
    private String newPassword;

    private ChangePassword(Long memberId, String originPassword, String newPassword) {
        this.memberId = memberId;
        this.originPassword = originPassword;
        this.newPassword = newPassword;
    }

    public static ChangePassword of(Long memberId, String originPassword, String newPassword) {
        return new ChangePassword(memberId, originPassword, newPassword);
    }
}
