package com.recipia.member.domain;


import com.recipia.member.domain.auditingfield.UpdateDateTime;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

/** 뱃지 */
@ToString(callSuper = true)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
public class Badge extends UpdateDateTime {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "badge_id", nullable = false)
    private Long id;                    // 벳지 pk

    @Column(name = "badge_nm", nullable = false)
    private String badgeName;           // 뱃지명

    @Column(name = "badge_dc", nullable = false)
    private String badgeDescription;    // 뱃지 설명

    @Column(name = "badge_achv")
    private Integer badgeAchieve;       // 뱃지 달성 조건

    @ToString.Exclude
    @OneToMany(mappedBy = "badge")
    List<MemberBadgeMap> badgeMapList = new ArrayList<>(); // 뱃지 매핑 리스트

    private Badge(String badgeName, String badgeDescription, Integer badgeAchieve) {
        this.badgeName = badgeName;
        this.badgeDescription = badgeDescription;
        this.badgeAchieve = badgeAchieve;
    }

    // factory method 선언
    public static Badge of(String badgeName, String badgeDescription, Integer badgeAchieve) {
        return new Badge(badgeName, badgeDescription, badgeAchieve);
    }
}
