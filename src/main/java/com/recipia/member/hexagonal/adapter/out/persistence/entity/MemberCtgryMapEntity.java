package com.recipia.member.hexagonal.adapter.out.persistence.entity;

import com.recipia.member.hexagonal.adapter.out.persistence.entity.auditingfield.UpdateDateTimeForEntity;
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
public class MemberCtgryMapEntity extends UpdateDateTimeForEntity {

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

    private MemberCtgryMapEntity(MemberEntity member, Long ctgryId) {
        this.member = member;
        this.ctgryId = ctgryId;
    }

    // 생성자 factory method 선언
    public static MemberCtgryMapEntity of(MemberEntity member, Long ctgryId) {
        return new MemberCtgryMapEntity(member, ctgryId);
    }

}
