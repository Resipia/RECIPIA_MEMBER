package com.recipia.member.common.event;

/**
 * 닉네임 변경 이벤트 객체
 * @param memberId member pk
 */
public record NicknameChangeSpringEvent(
        Long memberId
) {
    public static NicknameChangeSpringEvent of(Long memberId){
        return new NicknameChangeSpringEvent(memberId);
    }
}
