package com.recipia.member.hexagonal.adapter.out.persistence.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

/** 회원 뱃지 매핑 */
@ToString(callSuper = true)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
public class MemberBadgeMap {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_badge_map_id", nullable = false)
    private Long id;                  // 회원 뱃지 매핑 pk

    @ToString.Exclude
    @JoinColumn(name = "member_id", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private MemberEntity member;                // 회원 pk

    @ToString.Exclude
    @JoinColumn(name = "badge_id", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private BadgeEntity badgeEntity;              // 뱃지 pk

    @Column(name = "rep_badge_yn", nullable = false)
    private String representBadgeYn;  // 대표 뱃지 유무

    private MemberBadgeMap(MemberEntity member, BadgeEntity badgeEntity, String representBadgeYn) {
        this.member = member;
        this.badgeEntity = badgeEntity;
        this.representBadgeYn = representBadgeYn;
    }

    // factory method 선언
    public static MemberBadgeMap of(MemberEntity member, BadgeEntity badgeEntity, String representBadgeYn) {
        return new MemberBadgeMap(member, badgeEntity, representBadgeYn);
    }


}
