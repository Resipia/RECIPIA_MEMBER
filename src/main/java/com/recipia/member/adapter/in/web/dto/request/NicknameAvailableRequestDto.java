package com.recipia.member.adapter.in.web.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * 닉네임 중복체크를 요청하는 request dto
 */
@ToString
@Getter
@NoArgsConstructor
public class NicknameAvailableRequestDto {
    @NotBlank
    private String nickname;

    private NicknameAvailableRequestDto(String nickname) {
        this.nickname = nickname;
    }

    public static NicknameAvailableRequestDto of(String nickname) {
        return new NicknameAvailableRequestDto(nickname);
    }
}
