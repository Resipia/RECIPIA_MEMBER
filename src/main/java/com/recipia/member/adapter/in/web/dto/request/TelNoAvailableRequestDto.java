package com.recipia.member.adapter.in.web.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * 사용자가 입력한 전화번호가 이미 DB에 등록된 전화번호인지 확인 요청하는 Request Dto
 */
@ToString
@Getter
@NoArgsConstructor
public class TelNoAvailableRequestDto {

    @NotBlank
    @Pattern(regexp = "\\d+")       // 문자열 안에서 숫자만 입력 가능
    private String telNo;

    private TelNoAvailableRequestDto(String telNo) {
        this.telNo = telNo;
    }

    public static TelNoAvailableRequestDto of(String telNo) {
        return new TelNoAvailableRequestDto(telNo);
    }
}
