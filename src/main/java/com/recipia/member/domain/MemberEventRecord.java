package com.recipia.member.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 이벤트 저장소 도메인 객체
 */
@Getter
@NoArgsConstructor
public class MemberEventRecord {

    private Long id;
    private Long memberId;
    private String snsTopic;
    private String eventType;
    private String attribute;
    private String traceId;
    private boolean published;
    private LocalDateTime publishedAt;

    private MemberEventRecord(Long id, Long memberId, String snsTopic, String eventType, String attribute, String traceId, boolean published, LocalDateTime publishedAt) {
        this.id = id;
        this.memberId = memberId;
        this.snsTopic = snsTopic;
        this.eventType = eventType;
        this.attribute = attribute;
        this.traceId = traceId;
        this.published = published;
        this.publishedAt = publishedAt;
    }

    public static MemberEventRecord of(Long id, Long memberId, String snsTopic, String eventType, String attribute, String traceId, boolean published, LocalDateTime publishedAt) {
        return new MemberEventRecord(id, memberId, snsTopic, eventType, attribute, traceId, published, publishedAt);
    }

    /**
     * Listener에서 Service로 넘길때 사용할 도메인 팩토리 메서드
     * @param memberId
     * @param eventType
     * @return
     */
    public static MemberEventRecord of(Long memberId, String eventType, String topicName) {
        return new MemberEventRecord(null, memberId, topicName, eventType, null, null, false, null);
    }

    public static MemberEventRecord of(Long memberId, String snsTopic, String eventType, String attribute, String traceId, boolean published, LocalDateTime publishedAt) {
        return new MemberEventRecord(null, memberId, snsTopic, eventType, attribute, traceId, published, publishedAt);
    }

}
