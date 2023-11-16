package com.recipia.member.springevent;

/**
 * 닉네임 변경 이벤트 객체
 * @param memberId member pk
 */
public record NicknameChangeSpringEvent(
        Long memberId
) {
}
