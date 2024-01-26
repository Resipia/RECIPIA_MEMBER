package com.recipia.member.common.event;

/**
 * 회원 탈퇴 이벤트 객체
 */
public record MemberWithdrawSpringEvent(
        Long memberId
) {
}
