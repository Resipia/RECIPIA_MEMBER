package com.recipia.member.dto;

import com.recipia.member.hexagonal.adapter.out.persistence.entity.MemberHistoryLogEntity;

import java.time.LocalDateTime;


public record MemberHistoryLogDto(
        Long id,
        Long recipeId,
        Long wriggleId,
        LocalDateTime createDateTime
) {

    public static MemberHistoryLogDto of(Long id, Long recipeId, Long wriggleId, LocalDateTime createDateTime) {
        return new MemberHistoryLogDto(id, recipeId, wriggleId, createDateTime);
    }

    /** entity를 dto로 변환시켜주는 factory method */
    public static MemberHistoryLogDto fromEntity(MemberHistoryLogEntity entity) {
        return of(
                entity.getId(),
                entity.getRecipeId(),
                entity.getWriggleId(),
                entity.getCreateDateTime()
        );
    }

}