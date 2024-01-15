package com.recipia.member.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 팔로우 도메인 객체
 */
@Getter
@NoArgsConstructor
public class Follow {
    private Long followerMemberId;      // 팔로우를 요청한 memberId
    private Long followingMemberId;     // 팔로우 대상 memberId

    @Builder
    private Follow(Long followerMemberId, Long followingMemberId) {
        this.followerMemberId = followerMemberId;
        this.followingMemberId = followingMemberId;
    }

    public static Follow of(Long followerMemberId, Long followingMemberId) {
        return new Follow(followerMemberId, followingMemberId);
    }
}
