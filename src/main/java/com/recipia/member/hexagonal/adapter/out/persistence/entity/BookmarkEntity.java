package com.recipia.member.hexagonal.adapter.out.persistence.entity;

import com.recipia.member.hexagonal.adapter.out.persistence.entity.auditingfield.CreateDateTimeForEntity;
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
public class BookmarkEntity extends CreateDateTimeForEntity {

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

    private BookmarkEntity(MemberEntity member, Long recipeId) {
        this.member = member;
        this.recipeId = recipeId;
    }

    // factory method 선언
    public static BookmarkEntity of(MemberEntity member, Long recipeId) {
        return new BookmarkEntity(member, recipeId);
    }

}
