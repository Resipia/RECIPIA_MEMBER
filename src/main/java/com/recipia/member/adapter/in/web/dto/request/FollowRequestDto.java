package com.recipia.member.adapter.in.web.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * 사용자가 특정 멤버 팔로우를 요청하는 request dto
 */
@ToString
@Getter
@NoArgsConstructor
public class FollowRequestDto {

    // 팔로우 대상 회원의 pk
    @NotNull
    private Long followingMemberId;

    private FollowRequestDto(Long followingMemberId) {
        this.followingMemberId = followingMemberId;
    }

    public static FollowRequestDto of(Long followingMemberId) {
        return new FollowRequestDto(followingMemberId);
    }
}
