package com.recipia.member.dto;

import com.recipia.member.hexagonal.adapter.out.persistence.MemberBadgeMap;

public record MemberBadgeMapDto(
        Long id,
        String representBadgeYn
) {

    public static MemberBadgeMapDto of(Long id, String representBadgeYn) {
        return new MemberBadgeMapDto(id, representBadgeYn);
    }

    /** entity를 dto로 변환시켜주는 factory method */
    public static MemberBadgeMapDto fromEntity(MemberBadgeMap entity) {
        return of(
                entity.getId(),
                entity.getRepresentBadgeYn()
        );
    }

}