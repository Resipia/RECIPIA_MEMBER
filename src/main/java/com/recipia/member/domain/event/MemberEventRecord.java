package com.recipia.member.domain.event;


import com.recipia.member.domain.Member;
import com.recipia.member.domain.auditingfield.CreateDateTime;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDateTime;

/** 회원 이벤트 기록 */
@ToString(callSuper = true)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
public class MemberEventRecord extends CreateDateTime {

    // 회원 이벤트 기록 Pk
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_event_record_id", nullable = false)
    private Long id;

    // 회원 pk
    @ToString.Exclude
    @JoinColumn(name = "member_id", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private Member member;

    // sns topic 명
    @Column(name = "sns_topic", nullable = false)
    private String snsTopic;

    // Spring event 이벤트 객체
    @Column(name = "event_type", nullable = false)
    private String eventType;

    // sns 메시지 내용 (json 형태)
    @Column(name = "attribute", nullable = false)
    private String attribute;

    // sns 발행 여부
    @Column(name = "published", nullable = false)
    private boolean published;

    // sqs 에서 메시지 받은 시점
    @Column(name = "published_at")
    private LocalDateTime publishedAt;

    @Column(name = "is_batch")
    private Boolean isBatch;

    private MemberEventRecord(Member member, String snsTopic, String eventType, String attribute, boolean published, LocalDateTime publishedAt) {
        this.member = member;
        this.snsTopic = snsTopic;
        this.eventType = eventType;
        this.attribute = attribute;
        this.published = published;
        this.publishedAt = publishedAt;
    }

    // 생성자 factory method of 선언
    public static MemberEventRecord of(Member member, String snsTopic, String eventType, String attribute, boolean published, LocalDateTime publishedAt) {
        return new MemberEventRecord(member, snsTopic, eventType, attribute, published, publishedAt);
    }


    /**
     * MemberEventRecord published: true, publishedAt: now 업데이트 메소드
     */
    public void changePublished() {
        this.published = true;
        publishedAt = LocalDateTime.now();
    }

    /**
     * 배치처리할때 isBatch=true 로 업데이트 하는 메서드
     */
    public void changeIsBatchTrue() {
        this.isBatch = true;
    }

    /**
     * 배치처리할때 traceId 누락된 데이터는 새로 생성해서 DB에 attribute로 추가해주는 메소드
     * @param newAttribute 새로 생성된 traceId와 기존에 있던 memberId
     */
    public void changeAttribute(String newAttribute) {
        this.attribute = newAttribute;
    }

}
