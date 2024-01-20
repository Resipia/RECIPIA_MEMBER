package com.recipia.member.adapter.in.web.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * 비밀번호 재발급 요청을 담는 request dto
 */
@ToString
@Getter
@Setter
@NoArgsConstructor
public class TempPasswordRequestDto {

    @NotBlank
    private String email;

    private TempPasswordRequestDto(String email) {
        this.email = email;
    }

    public static TempPasswordRequestDto of(String email) {
        return new TempPasswordRequestDto(email);
    }
}
