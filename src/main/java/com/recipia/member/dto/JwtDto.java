package com.recipia.member.dto;

import com.recipia.member.hexagonal.adapter.out.persistence.JwtEntity;

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

    public static JwtDto fromEntity(JwtEntity entity) {
        return of(entity.getId(), MemberDto.fromEntity(entity.getMember()), entity.getRefreshToken(), entity.getExpiredDateTime());
    }
}
