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
    @Column(name = "member_event_record_pk", nullable = false)
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


}
