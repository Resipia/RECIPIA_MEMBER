package com.recipia.member.common.event;

/**
 * 회원가입 스프링 이벤트 발행
 * @param memberId 생성된 회원의 pk값
 */
public record SignUpSpringEvent(
        Long memberId
) {
    public static SignUpSpringEvent of(Long memberId) {
        return new SignUpSpringEvent(memberId);
    }
}
