package com.recipia.member.domain;

import com.recipia.member.adapter.out.persistence.constant.MemberStatus;
import com.recipia.member.adapter.out.persistence.constant.RoleType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Getter
@NoArgsConstructor
public class Member {

    private Long id;                // 회원 pk
    private String email;           // 이메일
    private String password;        // 회원 비밀번호
    private String fullName;        // 회원이름
    private String nickname;        // 닉네임
    private MemberStatus status;    // 회원상태
    private String introduction;    // 한줄소개
    private String telNo;           // 전화번호
    private String address1;        // 주소1
    private String address2;        // 주소2
    private RoleType roleType;      // 계정 권한

    private static final int MIN_PASSWORD_LENGTH = 8;
    private static final int MAX_PASSWORD_LENGTH = 20;

    private Member(Long id, String email, String password, String fullName, String nickname, MemberStatus status, String introduction, String telNo, String address1, String address2, RoleType roleType) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.fullName = fullName;
        this.nickname = nickname;
        this.status = status;
        this.introduction = introduction;
        this.telNo = telNo;
        this.address1 = address1;
        this.address2 = address2;
        this.roleType = roleType;
    }

    public static Member of(Long id, String email, String password, String fullName, String nickname, MemberStatus status, String introduction, String telNo, String address1, String address2, RoleType roleType) {
        return new Member(id, email, password, fullName, nickname, status, introduction, telNo, address1, address2, roleType);
    }

    /**
     * 비밀번호가 영어 대,소문자, 숫자, 특수문자가 하나씩 무조건 포함되어야하고 8글자 이상 20자 이하인지 검증하는 메소드
     * @param password
     * @return
     */
    public static boolean isValidPassword(String password) {
        if (password == null || password.length() < MIN_PASSWORD_LENGTH || password.length() > MAX_PASSWORD_LENGTH) {
            return false;
        }

        boolean hasUpper = false;
        boolean hasLower = false;
        boolean hasDigit = false;
        boolean hasSpecial = false;

        for (char ch : password.toCharArray()) {
            if (Character.isUpperCase(ch)) hasUpper = true;
            else if (Character.isLowerCase(ch)) hasLower = true;
            else if (Character.isDigit(ch)) hasDigit = true;
            else hasSpecial = true;

            if (hasUpper && hasLower && hasDigit && hasSpecial) {
                return true;
            }
        }

        return false;
    }

    /**
     * 비밀번호를 암호화하는 인스턴스 메소드
     */
    public void passwordEncoder() {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        this.password = encoder.encode(this.password);
    }


}

