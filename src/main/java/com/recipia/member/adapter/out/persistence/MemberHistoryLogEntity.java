package com.recipia.member.adapter.out.persistence;


import com.recipia.member.adapter.out.persistence.auditingfield.CreateDateTimeForEntity;
import com.recipia.member.adapter.out.persistence.constant.MemberActionType;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.Objects;

/** 회원 히스토리 로그 */
@ToString(callSuper = true)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
@Table(name = "member_hist_log")
public class MemberHistoryLogEntity extends CreateDateTimeForEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_hist_log_id", nullable = false)
    private Long id;            // 회원 히스토리 로그 pk

    @ToString.Exclude
    @JoinColumn(name = "member_id", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private MemberEntity member;          // 회원 pk

    @Column(name = "action", nullable = false)
    @Enumerated(EnumType.STRING)
    private MemberActionType action;

    @Column(name = "event_type", nullable = false)
    private String eventType;


    // private 생성자
    private MemberHistoryLogEntity(Long id, MemberEntity member, MemberActionType action, String eventType) {
        this.id = id;
        this.member = member;
        this.action = action;
        this.eventType = eventType;
    }

    // 새 엔티티 생성용 팩토리 메소드
    public static MemberHistoryLogEntity of(MemberEntity member, MemberActionType action, String eventType) {
        return new MemberHistoryLogEntity(null, member, action, eventType);
    }

    // 기존 엔티티 로드용 팩토리 메소드
    public static MemberHistoryLogEntity of(Long id, MemberEntity member, MemberActionType action, String eventType) {
        return new MemberHistoryLogEntity(id, member, action, eventType);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof MemberHistoryLogEntity that)) return false;
        return this.id != null && Objects.equals(getId(), that.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }
}
