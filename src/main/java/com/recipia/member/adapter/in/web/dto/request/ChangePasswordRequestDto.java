package com.recipia.member.adapter.in.web.dto.request;


import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * 비밀번호 재설정을 요청하는 request dto
 */
@ToString
@Getter
@NoArgsConstructor
public class ChangePasswordRequestDto {
    @NotBlank
    private String password;

    private ChangePasswordRequestDto(String password) {
        this.password = password;
    }

    public static ChangePasswordRequestDto of(String password) {
        return new ChangePasswordRequestDto(password);
    }
}
