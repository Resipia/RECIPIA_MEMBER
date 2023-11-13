package com.recipia.member.event;

/**
 * 닉네임 변경 이벤트 객체
 * @param memberId member pk
 */
public record NicknameChangeEvent(
        Long memberId
) {
}
