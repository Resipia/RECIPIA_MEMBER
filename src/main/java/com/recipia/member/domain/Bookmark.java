package com.recipia.member.domain;

import com.recipia.member.domain.auditingfield.CreateDateTime;
import com.recipia.member.hexagonal.adapter.out.persistence.member.MemberEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

/** 북마크 */
@ToString(callSuper = true)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
public class Bookmark extends CreateDateTime {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "bookmark_id", nullable = false)
    private Long id;            // 북마크 pk

    @ToString.Exclude
    @JoinColumn(name = "member_id", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private MemberEntity member;          // 회원 pk

    @Column(name = "recipe_id", nullable = false)
    private Long recipeId;      // 레시피 pk

    private Bookmark(MemberEntity member, Long recipeId) {
        this.member = member;
        this.recipeId = recipeId;
    }

    // factory method 선언
    public static Bookmark of(MemberEntity member, Long recipeId) {
        return new Bookmark(member, recipeId);
    }

}
