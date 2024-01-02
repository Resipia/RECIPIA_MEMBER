package com.recipia.member.adapter.in.web.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * 사용자가 입력한 인증코드 검증을 요청하는 Request Dto
 */
@ToString
@Getter
@NoArgsConstructor
public class CheckVerifyCodeRequestDto {

    @NotBlank
    private String phoneNumber;

    @NotBlank
    private String verifyCode;

    private CheckVerifyCodeRequestDto(String phoneNumber, String verifyCode) {
        this.phoneNumber = phoneNumber;
        this.verifyCode = verifyCode;
    }

    public static CheckVerifyCodeRequestDto of(String phoneNumber, String verifyCode) {
        return new CheckVerifyCodeRequestDto(phoneNumber, verifyCode);
    }
}
