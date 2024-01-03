package com.recipia.member.adapter.in.web.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * 휴대폰 번호 입력하고 인증코드 요청보내는 Request Dto
 */
@ToString
@Getter
@NoArgsConstructor
public class PhoneNumberRequestDto {

    @NotBlank
    @Pattern(regexp = "\\d+")
    private String phoneNumber;

    private PhoneNumberRequestDto(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public static PhoneNumberRequestDto of (String phoneNumber) {
        return new PhoneNumberRequestDto(phoneNumber);
    }
}
