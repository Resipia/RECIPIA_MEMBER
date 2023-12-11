package com.recipia.member.hexagonal.adapter.out.persistence.entity;


import com.recipia.member.hexagonal.adapter.out.persistence.entity.auditingfield.CreateDateTimeForEntity;
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
public class FollowEntity extends CreateDateTimeForEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "follow_id", nullable = false)
    private Long id;            // 팔로우 pk

    @ToString.Exclude
    @JoinColumn(name = "follower_member_id", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private MemberEntity followerMember;  // 팔로워 회원 pk

    @ToString.Exclude
    @JoinColumn(name = "following_member_id", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private MemberEntity followingMember; // 팔로잉 회원 pk

    private FollowEntity(MemberEntity followerMember, MemberEntity followingMember) {
        this.followerMember = followerMember;
        this.followingMember = followingMember;
    }

    // 생성자 factory method 선언
    public static FollowEntity of(MemberEntity followerMember, MemberEntity followingMember) {
        return new FollowEntity(followerMember, followingMember);
    }

}
