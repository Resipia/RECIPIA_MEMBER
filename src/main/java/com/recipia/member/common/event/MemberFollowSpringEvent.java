package com.recipia.member.common.event;

public record MemberFollowSpringEvent (
        Long followerId,        // 팔로워 member id
        Long targetMemberId     // 알림받을 회원 member id
) {
    public static MemberFollowSpringEvent of(Long followerId, Long targetMemberId) {
        return new MemberFollowSpringEvent(followerId, targetMemberId);
    }
}
