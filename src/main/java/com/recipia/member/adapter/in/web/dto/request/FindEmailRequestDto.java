package com.recipia.member.adapter.in.web.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * 회원의 이메일 찾기 요청하는 request dto
 */
@ToString
@Getter
@NoArgsConstructor
public class FindEmailRequestDto {

    @NotBlank
    private String fullName;

    @NotBlank
    @Pattern(regexp = "\\d+")       // 문자열 안에서 숫자만 입력 가능
    private String telNo;

    private FindEmailRequestDto(String fullName, String telNo) {
        this.fullName = fullName;
        this.telNo = telNo;
    }

    public static FindEmailRequestDto of(String fullName, String telNo) {
        return new FindEmailRequestDto(fullName, telNo);
    }
}
