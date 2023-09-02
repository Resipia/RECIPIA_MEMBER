package com.recipia.member.dto;

import com.recipia.member.domain.Jwt;

import java.time.LocalDateTime;

public record JwtDto(
        Long id,
        MemberDto memberDto,
        String refreshToken,
        LocalDateTime expiredDateTime
) {

    public static JwtDto of(Long id, MemberDto memberDto, String refreshToken, LocalDateTime expiredDateTime) {
        return new JwtDto(id, memberDto, refreshToken, expiredDateTime);
    }

    public static JwtDto fromEntity(Jwt entity) {
        return of(entity.getId(), MemberDto.fromEntity(entity.getMember()), entity.getRefreshToken(), entity.getExpiredDateTime());
    }
}
