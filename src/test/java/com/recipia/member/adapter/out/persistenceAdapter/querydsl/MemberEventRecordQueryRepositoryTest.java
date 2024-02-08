package com.recipia.member.adapter.out.persistenceAdapter.querydsl;

import com.recipia.member.config.TotalTestSupport;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.jupiter.api.Assertions.assertEquals;

@Transactional
@DisplayName("[통합] 멤버 서버 이벤트 저장소 query repository 테스트")
class MemberEventRecordQueryRepositoryTest extends TotalTestSupport {

    @Autowired
    private MemberEventRecordQueryRepository sut;

    @DisplayName("[happy] message, snsTopic이 동일한 이벤트중 가장 최근에 발행된 이벤트를 published = true로 업데이트 성공")
    @Test
    void latestEventChangePublishedToTrueSuccess() {
        // given
        String message = "attribute1";
        String snsTopic = "topic1";

        // when
        Long updatedCount = sut.changePublishedToTrue(message, snsTopic);

        // then
        assertEquals(1L, updatedCount); // 하나의 레코드가 업데이트 되었는지 확인
    }

    @DisplayName("[bad] message, snsTopic이 동일한 이벤트중 가장 최근에 발행한 이벤트중에서 false인 데이터가 없을때 published = true 업데이트 실패")
    @Test
    void testChangePublishedToTrueBadCase() {
        // given
        String message = "non-existing-message";
        String snsTopic = "nonExistingTopic";

        // when
        Long updatedCount = sut.changePublishedToTrue(message, snsTopic);

        // then
        assertEquals(0L, updatedCount); // 어떠한 레코드도 업데이트되지 않았는지 확인
    }

    @DisplayName("[happy] 동일한 멤버, 동일한 토픽일때 기존에 발행 실패했던 데이터 전부 발행 성공 처리(published = true) 성공")
    @Test
    void changeBeforeEventAllPublishedToTrueSuccess() {
        // given
        Long memberId = 1L;
        String topicName = "topic1";

        // when
        Long updatedCount = sut.changeBeforeEventAllPublishedToTrue(memberId, topicName);

        // then
        assertEquals(3, updatedCount);  // 현재 data.sql에 위 조건에 해당하는 row 갯수는 3개다.
    }

}