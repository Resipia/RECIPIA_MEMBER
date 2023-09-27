package com.recipia.member.domain;


import com.recipia.member.domain.auditingfield.CreateDateTime;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

/** 팔로우 */
@ToString(callSuper = true)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
public class Follow extends CreateDateTime {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "follow_id", nullable = false)
    private Long id;            // 팔로우 pk

    @ToString.Exclude
    @JoinColumn(name = "follower_member_id", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private Member followerMember;  // 팔로워 회원 pk

    @ToString.Exclude
    @JoinColumn(name = "following_member_id", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private Member followingMember; // 팔로잉 회원 pk

    private Follow(Member followerMember, Member followingMember) {
        this.followerMember = followerMember;
        this.followingMember = followingMember;
    }

    // 생성자 factory method 선언
    public static Follow of(Member followerMember, Member followingMember) {
        return new Follow(followerMember, followingMember);
    }

}