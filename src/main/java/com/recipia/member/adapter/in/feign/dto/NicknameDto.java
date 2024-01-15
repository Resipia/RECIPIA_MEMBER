package com.recipia.member.adapter.in.feign.dto;

/**
 * FeignClient 응답 DTO
 * Member 서버에서 받은 memberId로 받은 새로운 nickname 데이터를 저장하는 DTO
 */
public record NicknameDto(
        Long memberId,
        String nickname
) {

    public static NicknameDto of(Long memberId, String nickname) {
        return new NicknameDto(memberId, nickname);
    }

}
