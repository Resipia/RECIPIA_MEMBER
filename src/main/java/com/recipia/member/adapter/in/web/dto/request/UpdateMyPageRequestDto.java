package com.recipia.member.adapter.in.web.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@ToString
@Getter
@NoArgsConstructor
public class UpdateMyPageRequestDto {

    @NotBlank
    private String nickname;        // 닉네임

    private String introduction;    // 한줄소개

    private UpdateMyPageRequestDto(String nickname, String introduction) {
        this.nickname = nickname;
        this.introduction = introduction;
    }

    public static UpdateMyPageRequestDto of(String nickname, String introduction) {
        return new UpdateMyPageRequestDto(nickname, introduction);
    }
}
