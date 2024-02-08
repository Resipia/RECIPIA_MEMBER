package com.recipia.member.adapter.out.persistenceAdapter;

import com.recipia.member.config.TotalTestSupport;
import jakarta.transaction.Transactional;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@Transactional
@DisplayName("[통합] 이벤트 저장소 Adapter 테스트")
class MemberEventRecordAdapterTest extends TotalTestSupport {

    @Autowired
    private MemberEventRecordAdapter sut;

    @DisplayName("[happy] message, topciName을 받아와서 published = false인 이벤트중 가장 최신 이벤트의 published = true로 바꿔준다.")
    @Test
    public void changePublishedToTrue () {

        //given
        String message = "attribute1";
        String topicName = "topic1";

        //when
        Long changedCount = sut.changePublishedToTrue(message, topicName);

        //then
        Assertions.assertThat(changedCount).isEqualTo(1L);
    }

    @DisplayName("[happy] memberId, topicName을 받아와서 이전에 누락된 이벤트들의 published = true로 변경한다.")
    @Test
    public void changeBeforeEventAllPublishedToTrue() {
        // given
        Long memberId = 1L;
        String topicName = "topic1";

        // when
        Long changedCount = sut.changeBeforeEventAllPublishedToTrue(memberId, topicName);

        // then
        Assertions.assertThat(changedCount).isPositive(); // 변경된 이벤트 수가 양수인지 확인
    }

}