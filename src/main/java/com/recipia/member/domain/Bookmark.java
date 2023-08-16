package com.recipia.member.domain;

import com.recipia.member.domain.auditingfield.CreateDateTime;
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
    @JoinColumn(name = "user_id", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private Member member;          // 회원 pk

    @Column(name = "recipe_id", nullable = false)
    private Long recipeId;      // 레시피 pk

    private Bookmark(Member member, Long recipeId) {
        this.member = member;
        this.recipeId = recipeId;
    }

    // factory method 선언
    public static Bookmark of(Member member, Long recipeId) {
        return new Bookmark(member, recipeId);
    }

}
