package com.recipia.member.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 임시 비밀번호 도메인 객체
 */
@Getter
@NoArgsConstructor
public class TempPassword {

    private String name;
    private String telNo;
    private String email;
    private String tempPassword;

    private TempPassword(String name, String telNo, String email, String tempPassword) {
        this.name = name;
        this.telNo = telNo;
        this.email = email;
        this.tempPassword = tempPassword;
    }

    public static TempPassword of(String name, String telNo, String email, String tempPassword) {
        return new TempPassword(name, telNo, email, tempPassword);
    }

    public static TempPassword of(String name, String telNo, String email) {
        return new TempPassword(name, telNo, email, null);
    }

}
