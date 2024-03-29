package com.recipia.member.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 본인 인증 도메인 객체
 */
@Getter
@NoArgsConstructor
public class Authentication {

    private String phoneNumber;
    private String email;
    private String verifyCode;

    @Builder
    private Authentication(String phoneNumber, String email, String verifyCode) {
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.verifyCode = verifyCode;
    }

    public static Authentication of(String phoneNumber, String email, String verifyCode) {
        return new Authentication(phoneNumber, email, verifyCode);
    }

    /**
     * 사용자가 입력한 휴대폰 번호를 문자 발행에 적합한 형태로 변환해주는 메서드
     * @return +821000000000 형태로 전환된 휴대폰 번호
     */
    public void formatPhoneNumber() {
        String cleanedNumber = phoneNumber.replaceAll("\\D", ""); // 특수문자와 공백 제거
        this.phoneNumber = "+82" + cleanedNumber; // 국가 코드 추가
    }

}
