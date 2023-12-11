package com.recipia.member.dto;

import com.recipia.member.hexagonal.adapter.out.persistence.entity.FollowEntity;

import java.time.LocalDateTime;

public record FollowDto(
        Long id,
        LocalDateTime createDateTime
) {

    public static FollowDto of(Long id, LocalDateTime createDateTime) {
        return new FollowDto(id, createDateTime);
    }

    /** entity를 dto로 변환시켜주는 factory method */
    public static FollowDto fromEntity(FollowEntity entity) {
        return of(
                entity.getId(),
                entity.getCreateDateTime()
        );
    }
}