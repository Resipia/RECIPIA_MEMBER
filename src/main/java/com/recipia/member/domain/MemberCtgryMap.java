package com.recipia.member.domain;

import com.recipia.member.domain.auditingfield.UpdateDateTime;
import com.recipia.member.hexagonal.adapter.out.persistence.member.MemberEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

/** 회원 카테고리 매핑 */
@ToString(callSuper = true)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
public class MemberCtgryMap extends UpdateDateTime {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_ctgry_map_id", nullable = false)
    private Long id;        // 회원 카테고리 매핑 pk

    @ToString.Exclude
    @JoinColumn(name = "member_id", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private MemberEntity member;      // 회원 pk

    @Column(name = "ctgry_id", nullable = false)
    private Long ctgryId;   // 음식 카테고리 pk

    private MemberCtgryMap(MemberEntity member, Long ctgryId) {
        this.member = member;
        this.ctgryId = ctgryId;
    }

    // 생성자 factory method 선언
    public static MemberCtgryMap of(MemberEntity member, Long ctgryId) {
        return new MemberCtgryMap(member, ctgryId);
    }

}
