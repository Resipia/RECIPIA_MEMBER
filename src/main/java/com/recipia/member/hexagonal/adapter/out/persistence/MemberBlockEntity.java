package com.recipia.member.hexagonal.adapter.out.persistence;

import com.recipia.member.hexagonal.adapter.out.persistence.auditingfield.CreateDateTimeForEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.Objects;

/** 회원 차단 */
@ToString(callSuper = true)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
public class MemberBlockEntity extends CreateDateTimeForEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_block_id", nullable = false)
    private Long id;        // 회원 차단 pk

    @ToString.Exclude
    @JoinColumn(name = "member_id", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private MemberEntity member;      // 회원 pk

    @ToString.Exclude
    @JoinColumn(name = "block_member_id", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private MemberEntity blockMember; // 차단 회원 pk

    private MemberBlockEntity(Long id, MemberEntity member, MemberEntity blockMember) {
        this.id = id;
        this.member = member;
        this.blockMember = blockMember;
    }

    // 새 엔티티 생성용 팩토리 메소드
    public static MemberBlockEntity of(MemberEntity member, MemberEntity blockMember) {
        return new MemberBlockEntity(null, member, blockMember);
    }

    // 기존 엔티티 로드용 팩토리 메소드
    public static MemberBlockEntity of(Long id, MemberEntity member, MemberEntity blockMember) {
        return new MemberBlockEntity(id, member, blockMember);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof MemberBlockEntity that)) return false;
        return this.id != null && Objects.equals(getId(), that.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }
}
