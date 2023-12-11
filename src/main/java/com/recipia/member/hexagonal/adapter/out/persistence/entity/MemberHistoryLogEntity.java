package com.recipia.member.hexagonal.adapter.out.persistence.entity;


import com.recipia.member.hexagonal.adapter.out.persistence.entity.auditingfield.CreateDateTimeForEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

/** 회원 히스토리 로그 */
@ToString(callSuper = true)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
public class MemberHistoryLogEntity extends CreateDateTimeForEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_hist_log_id", nullable = false)
    private Long id;            // 회원 히스토리 로그 pk

    @ToString.Exclude
    @JoinColumn(name = "member_id", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private MemberEntity member;          // 회원 pk

    @Column(name = "recipe_id", nullable = false)
    private Long recipeId;      // 레시피 pk

    @Column(name = "wriggle_id", nullable = false)
    private Long wriggleId;     // 위글 pk

    private MemberHistoryLogEntity(MemberEntity member, Long recipeId, Long wriggleId) {
        this.member = member;
        this.recipeId = recipeId;
        this.wriggleId = wriggleId;
    }

    // factory method 선언
    public static MemberHistoryLogEntity of(MemberEntity member, Long recipeId, Long wriggleId) {
        return new MemberHistoryLogEntity(member, recipeId, wriggleId);
    }

}
