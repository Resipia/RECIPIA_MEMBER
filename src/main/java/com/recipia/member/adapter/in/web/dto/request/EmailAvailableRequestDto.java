package com.recipia.member.adapter.in.web.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * 사용자가 입력한 이메일이 이미 DB에 등록된 이메일인지 확인 요청하는 Request Dto
 */
@ToString
@Getter
@NoArgsConstructor
public class EmailAvailableRequestDto {

    @NotBlank
    private String email;

    private EmailAvailableRequestDto(String email) {
        this.email = email;
    }

    public static EmailAvailableRequestDto of(String email) {
        return new EmailAvailableRequestDto(email);
    }
}
