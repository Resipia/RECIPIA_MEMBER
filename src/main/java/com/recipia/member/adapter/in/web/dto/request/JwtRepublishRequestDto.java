package com.recipia.member.adapter.in.web.dto.request;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@ToString
@Getter
@NoArgsConstructor
public class JwtRepublishRequestDto {

    @NotNull
    private Long memberId;

    @NotBlank
    private String refreshToken;

    private JwtRepublishRequestDto(Long memberId, String refreshToken) {
        this.memberId = memberId;
        this.refreshToken = refreshToken;
    }

    public static JwtRepublishRequestDto of(Long memberId, String refreshToken) {
        return new JwtRepublishRequestDto(memberId, refreshToken);

    }
}
