package com.recipia.member.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
@NoArgsConstructor
public class Follow {
    private Long followerMemberId;
    private Long followingMemberId;

    private Follow(Long followerMemberId, Long followingMemberId) {
        this.followerMemberId = followerMemberId;
        this.followingMemberId = followingMemberId;
    }

    public static Follow of(Long followerMemberId, Long followingMemberId) {
        return new Follow(followerMemberId, followingMemberId);
    }
}
