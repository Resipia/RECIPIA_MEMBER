package com.recipia.member.common.event;

/**
 * 팔로우 요청에 성공했을때 발행되는 스프링 이벤트
 * @param followerId 팔로우를 건 팔로워 memberId
 * @param targetMemberId 팔로우를 당한 memberId
 */
public record MemberFollowSpringEvent (
        Long followerId,        // 팔로워 member id
        Long targetMemberId     // 알림받을 회원 member id
) {
    public static MemberFollowSpringEvent of(Long followerId, Long targetMemberId) {
        return new MemberFollowSpringEvent(followerId, targetMemberId);
    }
}
