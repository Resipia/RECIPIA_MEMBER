package com.recipia.member.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 회원 관리 도메인
 * 이메일 중복, 휴대폰 번호 중복 체크에 사용된다.
 */
@Getter
@NoArgsConstructor
public class MemberManagement {

    private String email;           // 이메일
    private String phoneNumber;     // 휴대폰 번호

    private static final String EMAIL_REGEX = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6}$";
    private static final Pattern EMAIL_PATTERN = Pattern.compile(EMAIL_REGEX);

    /**
     * 유효한 이메일인지 검증하는 메소드
     * @param email
     * @return
     */
    public static boolean isValidEmail(String email) {
        Matcher matcher = EMAIL_PATTERN.matcher(email);
        return matcher.matches();
    }


}
