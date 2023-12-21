package com.recipia.member.hexagonal.adapter.out.persistenceAdapter;

import com.recipia.member.hexagonal.adapter.out.persistenceAdapter.querydsl.MemberEventRecordQueryRepository;
import jakarta.transaction.Transactional;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles(value = "test")
@SpringBootTest
class MemberEventRecordAdapterTest {

    @Autowired
    private MemberEventRecordQueryRepository memberEventRecordQueryRepository;

    @DisplayName("memberId, topciName을 받아와서 published = false인 이벤트중 가장 최신 이벤트의 published = true로 바꿔준다.")
    @Test
    @Transactional
    public void changePublishedToTrue () {

        //given
        Long memberId = 1L;
        String topicName = "topic1";


        //when
        long changedCount = memberEventRecordQueryRepository.changePublishedToTrue(memberId, topicName);


        //then
        Assertions.assertThat(changedCount).isEqualTo(1);
    }

}