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

    // 팔로우, 언팔로우 대상 회원의 pk
    @NotNull
    private Long targetMemberId;

    private FollowRequestDto(Long targetMemberId) {
        this.targetMemberId = targetMemberId;
    }

    public static FollowRequestDto of(Long targetMemberId) {
        return new FollowRequestDto(targetMemberId);
    }
}
