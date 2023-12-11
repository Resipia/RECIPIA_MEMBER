package com.recipia.member.dto;

import com.recipia.member.hexagonal.adapter.out.persistence.BadgeEntity;

import java.time.LocalDateTime;


public record BadgeDto(
        Long id,
        String badgeName,
        String badgeDescription,
        Integer badgeArchive,
        LocalDateTime createDateTime,
        LocalDateTime updateDateTime
) {

    public static BadgeDto of(Long id, String badgeName, String badgeDescription, Integer badgeArchive, LocalDateTime createDateTime, LocalDateTime updateDateTime) {
        return new BadgeDto(id, badgeName, badgeDescription, badgeArchive, createDateTime, updateDateTime);
    }

    /** entity를 dto로 만드는 factory method 선언 */
    public static BadgeDto fromEntity(BadgeEntity entity) {
        return of(
                entity.getId(),
                entity.getBadgeName(),
                entity.getBadgeDescription(),
                entity.getBadgeAchieve(),
                entity.getCreateDateTime(),
                entity.getUpdateDateTime()
        );
    }

}