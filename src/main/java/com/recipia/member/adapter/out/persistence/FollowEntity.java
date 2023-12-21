package com.recipia.member.adapter.out.persistence;


import com.recipia.member.adapter.out.persistence.auditingfield.CreateDateTimeForEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.Objects;

/** 팔로우 */
@ToString(callSuper = true)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
@Table(name = "follow")
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

    private FollowEntity(Long id, MemberEntity followerMember, MemberEntity followingMember) {
        this.id = id;
        this.followerMember = followerMember;
        this.followingMember = followingMember;
    }

    // 새 엔티티 생성용 팩토리 메소드
    public static FollowEntity of(MemberEntity followerMember, MemberEntity followingMember) {
        return new FollowEntity(null, followerMember, followingMember);
    }

    // 기존 엔티티 로드용 팩토리 메소드
    public static FollowEntity of(Long id, MemberEntity followerMember, MemberEntity followingMember) {
        return new FollowEntity(id, followerMember, followingMember);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof FollowEntity that)) return false;
        return this.id != null && Objects.equals(getId(), that.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }
}
