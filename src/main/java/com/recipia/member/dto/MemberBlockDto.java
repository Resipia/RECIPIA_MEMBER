package com.recipia.member.dto;

import com.recipia.member.domain.MemberBlock;

import java.time.LocalDateTime;

public record MemberBlockDto(
        Long id,
        LocalDateTime createDateTime
) {

    public static MemberBlockDto of(Long id, LocalDateTime createDateTime) {
        return new MemberBlockDto(id, createDateTime);
    }

    /** entity를 dto로 변환시켜주는 factory method */
    public static MemberBlockDto fromEntity(MemberBlock entity) {
        return of(
                entity.getId(),
                entity.getCreateDateTime()
        );
    }

}