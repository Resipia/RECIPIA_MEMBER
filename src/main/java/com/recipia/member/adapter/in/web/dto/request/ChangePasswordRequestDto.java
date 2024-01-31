package com.recipia.member.adapter.in.web.dto.request;


import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * 비밀번호 재설정을 요청하는 request dto
 */
@Data
@NoArgsConstructor
public class ChangePasswordRequestDto {
    @NotBlank
    private String originPassword;
    @NotBlank
    private String newPassword;

    private ChangePasswordRequestDto(String originPassword, String newPassword) {
        this.originPassword = originPassword;
        this.newPassword = newPassword;
    }

    public static ChangePasswordRequestDto of(String originPassword, String newPassword) {
        return new ChangePasswordRequestDto(originPassword, newPassword);
    }
}
