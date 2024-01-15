package com.recipia.member.adapter.in.web.dto.response;

import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * access token 재발행 응답 dto
 */
@Getter
@NoArgsConstructor
public class JwtRepublishResponseDto {
    private String accessToken;

    private JwtRepublishResponseDto(String accessToken) {
        this.accessToken = accessToken;
    }

    public static JwtRepublishResponseDto of(String accessToken) {
        return new JwtRepublishResponseDto(accessToken);
    }
}
