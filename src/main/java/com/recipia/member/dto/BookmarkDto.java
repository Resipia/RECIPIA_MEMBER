package com.recipia.member.dto;

import com.recipia.member.domain.Bookmark;

import java.time.LocalDateTime;


public record BookmarkDto(
        Long id,
        Long recipeId,
        LocalDateTime createDateTime
) {

    public static BookmarkDto of(Long id, Long recipeId, LocalDateTime createDateTime) {
        return new BookmarkDto(id, recipeId, createDateTime);
    }

    /** entity를 dto로 변환시켜주는 factory method */
    public static BookmarkDto fromEntity(Bookmark entity) {
        return of(
                entity.getId(),
                entity.getRecipeId(),
                entity.getCreateDateTime()
        );
    }

}