package com.recipia.member.dto;

import com.recipia.member.hexagonal.adapter.out.persistence.entity.MemberCtgryMapEntity;

import java.time.LocalDateTime;

public record MemberCtgryMapDto(
        Long id,
        Long ctgryId,
        LocalDateTime createDateTime,
        LocalDateTime updateDateTime
) {

    public static MemberCtgryMapDto of(Long id, Long ctgryId, LocalDateTime createDateTime, LocalDateTime updateDateTime) {
        return new MemberCtgryMapDto(id, ctgryId, createDateTime, updateDateTime);
    }

    /** entity를 dto로 변환시켜주는 factory method */
    public static MemberCtgryMapDto fromEntity(MemberCtgryMapEntity entity) {
        return of(
                entity.getId(),
                entity.getCtgryId(),
                entity.getCreateDateTime(),
                entity.getUpdateDateTime()
        );
    }

}