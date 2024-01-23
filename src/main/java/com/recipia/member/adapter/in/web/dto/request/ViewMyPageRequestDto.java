package com.recipia.member.adapter.in.web.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * 다른 유저의 마이페이지 조회 요청을 담당하는 request dto
 */
@ToString
@Getter
@NoArgsConstructor
public class ViewMyPageRequestDto {

    @NotNull
    private Long targetMemberId;    // 마이페이지 조회 요청을 당한 회원 memberId

    private ViewMyPageRequestDto(Long targetMemberId) {
        this.targetMemberId = targetMemberId;
    }

    public static ViewMyPageRequestDto of(Long targetMemberId) {
        return new ViewMyPageRequestDto(targetMemberId);
    }
}
