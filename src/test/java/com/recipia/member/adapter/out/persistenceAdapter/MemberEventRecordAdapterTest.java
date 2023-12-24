package com.recipia.member.adapter.out.persistenceAdapter;

import com.recipia.member.config.TotalTestSupport;
import jakarta.transaction.Transactional;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@DisplayName("[통합] 이벤트 저장소 Adapter 테스트")
class MemberEventRecordAdapterTest extends TotalTestSupport {

    @Autowired
    private MemberEventRecordAdapter sut;

    @DisplayName("memberId, topciName을 받아와서 published = false인 이벤트중 가장 최신 이벤트의 published = true로 바꿔준다.")
    @Test
    @Transactional
    public void changePublishedToTrue () {

        //given
        Long memberId = 1L;
        String topicName = "topic1";

        //when
        long changedCount = sut.changePublishedToTrue(memberId, topicName);


        //then
        Assertions.assertThat(changedCount).isEqualTo(1);
    }

}