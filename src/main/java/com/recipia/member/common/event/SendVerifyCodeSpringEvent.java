package com.recipia.member.common.event;

/**
 * 휴대폰 번호로 문자 메시지 전송 이벤트 객체
 * @param phoneNumber +8201000000000 형태의 사용자 휴대폰 번호
 * @param verificationCode 문자 메시지에 포함된 인증코드 (숫자 6자리)
 */
public record SendVerifyCodeSpringEvent (
        String phoneNumber,
        String verificationCode
) {

    public static SendVerifyCodeSpringEvent of(String phoneNumber, String verificationCode) {
        return new SendVerifyCodeSpringEvent(phoneNumber, verificationCode);
    }

}
