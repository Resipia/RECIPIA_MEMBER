package com.recipia.member.adapter.out.persistenceAdapter.querydsl;

import com.recipia.member.adapter.in.web.dto.response.AskListResponseDto;
import com.recipia.member.config.TotalTestSupport;
import com.recipia.member.domain.Ask;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("[통합] 문의사항 querydsl 테스트")
class AskQueryRepositoryTest extends TotalTestSupport {

    @Autowired
    private AskQueryRepository sut;

    @DisplayName("[happy] 내가 작성한 문의사항 목록을 가져온다.")
    @Test
    void getAskList() {
        // given
        Long memberId = 1L;
        Pageable pageable = PageRequest.of(0, 5);

        // when
        Page<AskListResponseDto> result = sut.getAskList(memberId, pageable);
        // then
        assertThat(result).isNotNull();
        assertThat(result.getContent()).isNotEmpty();
    }

    @DisplayName("[happy] 내가 작성한 문의사항이 없다면 totalCount를 0으로 반환한다.")
    @Test
    void getNoneAskList() {
        // given
        Long memberId = 4L;
        Pageable pageable = PageRequest.of(0, 5);

        // when
        Page<AskListResponseDto> result = sut.getAskList(memberId, pageable);
        // then
        assertThat(result).isNotNull();
        assertThat(result.getTotalElements()).isEqualTo(0L);
    }

    @DisplayName("[happy] 존재하는 askId가 들어왔을때 해당 문의사항의 상세정보를 가져온다.")
    @Test
    void getAskDetail() {
        // given
        Ask domain = Ask.of(1L, 1L);
        // when
        Ask askDetail = sut.getAskDetail(domain);
        // then
        assertThat(askDetail).isNotNull();
    }

    @DisplayName("[happy] 존재하지 않는 askId가 들어왔을때 null을 반환한다.")
    @Test
    void getNoneAskDetail() {
        // given
        Ask domain = Ask.of(99L, 1L);
        // when
        Ask askDetail = sut.getAskDetail(domain);
        // then
        assertThat(askDetail).isNull();
    }
}