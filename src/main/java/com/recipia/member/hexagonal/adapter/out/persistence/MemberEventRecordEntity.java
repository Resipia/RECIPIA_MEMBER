package com.recipia.member.hexagonal.adapter.out.persistence;


import com.recipia.member.hexagonal.adapter.out.persistence.auditingfield.CreateDateTimeForEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.Objects;

/** 회원 이벤트 기록 */
@ToString(callSuper = true)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
public class MemberEventRecordEntity extends CreateDateTimeForEntity {

    // 회원 이벤트 기록 Pk
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_event_record_id", nullable = false)
    private Long id;

    // 회원 pk
    @ToString.Exclude
    @JoinColumn(name = "member_id", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private MemberEntity member;

    // sns topic 명
    @Column(name = "sns_topic", nullable = false)
    private String snsTopic;

    // Spring event 이벤트 객체
    @Column(name = "event_type", nullable = false)
    private String eventType;

    // sns 메시지 내용 (json 형태)
    @Column(name = "attribute", nullable = false)
    private String attribute;

    // zipkin trace id
    @Column(name = "trace_id")
    private String traceId;

    // sns 발행 여부
    @Column(name = "published", nullable = false)
    private boolean published;

    // sqs 에서 메시지 받은 시점
    @Column(name = "published_at")
    private LocalDateTime publishedAt;


    private MemberEventRecordEntity(Long id, MemberEntity member, String snsTopic, String eventType, String attribute, String traceId, boolean published, LocalDateTime publishedAt) {
        this.id = id;
        this.member = member;
        this.snsTopic = snsTopic;
        this.eventType = eventType;
        this.attribute = attribute;
        this.traceId = traceId;
        this.published = published;
        this.publishedAt = publishedAt;
    }

    // 새 엔티티 생성용 팩토리 메소드
    public static MemberEventRecordEntity of(MemberEntity member, String snsTopic, String eventType, String attribute, String traceId, boolean published, LocalDateTime publishedAt) {
        return new MemberEventRecordEntity(null, member, snsTopic, eventType, attribute, traceId, published, publishedAt);
    }

    // 기존 엔티티 로드용 팩토리 메소드
    public static MemberEventRecordEntity of(Long id, MemberEntity member, String snsTopic, String eventType, String attribute, String traceId, boolean published, LocalDateTime publishedAt) {
        return new MemberEventRecordEntity(id, member, snsTopic, eventType, attribute, traceId, published, publishedAt);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof MemberEventRecordEntity that)) return false;
        return this.id != null && Objects.equals(getId(), that.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }

    /**
     * MemberEventRecord published: true, publishedAt: now 업데이트 메소드
     */
    public void changePublishedToTrue() {
        this.published = true;
        publishedAt = LocalDateTime.now();
    }

}
