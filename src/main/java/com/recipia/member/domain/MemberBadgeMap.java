package com.recipia.member.domain;

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
    @Column(name = "user_badge_map_id", nullable = false)
    private Long id;                  // 회원 뱃지 매핑 pk

    @ToString.Exclude
    @JoinColumn(name = "user_id", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private Member member;                // 회원 pk

    @ToString.Exclude
    @JoinColumn(name = "badge_id", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private Badge badge;              // 뱃지 pk

    @Column(name = "rep_badge_yn", nullable = false)
    private String representBadgeYn;  // 대표 뱃지 유무

    private MemberBadgeMap(Member member, Badge badge, String representBadgeYn) {
        this.member = member;
        this.badge = badge;
        this.representBadgeYn = representBadgeYn;
    }

    // factory method 선언
    public static MemberBadgeMap of(Member member, Badge badge, String representBadgeYn) {
        return new MemberBadgeMap(member, badge, representBadgeYn);
    }


}
