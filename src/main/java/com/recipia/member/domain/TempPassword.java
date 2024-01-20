package com.recipia.member.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 * 임시 비밀번호 도메인 객체
 */
@Getter
@NoArgsConstructor
public class TempPassword {

    private String email;
    private String tempPassword;

    private TempPassword(String email, String tempPassword) {
        this.email = email;
        this.tempPassword = tempPassword;
    }

    public static TempPassword of(String email, String tempPassword) {
        return new TempPassword(email, tempPassword);
    }

    public static TempPassword of(String email) {
        return new TempPassword(email, null);
    }

}
