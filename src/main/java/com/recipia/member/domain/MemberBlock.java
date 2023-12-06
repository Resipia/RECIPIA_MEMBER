package com.recipia.member.domain;

import com.recipia.member.domain.auditingfield.CreateDateTime;
import com.recipia.member.hexagonal.adapter.out.persistence.member.Member;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

/** 회원 차단 */
@ToString(callSuper = true)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
public class MemberBlock extends CreateDateTime {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_block_id", nullable = false)
    private Long id;        // 회원 차단 pk

    @ToString.Exclude
    @JoinColumn(name = "member_id", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private Member member;      // 회원 pk

    @ToString.Exclude
    @JoinColumn(name = "block_member_id", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private Member blockMember; // 차단 회원 pk

    private MemberBlock(Member member, Member blockMember) {
        this.member = member;
        this.blockMember = blockMember;
    }

    // factory method of 선언
    public static MemberBlock of(Member member, Member blockMember) {
        return new MemberBlock(member, blockMember);
    }

}
