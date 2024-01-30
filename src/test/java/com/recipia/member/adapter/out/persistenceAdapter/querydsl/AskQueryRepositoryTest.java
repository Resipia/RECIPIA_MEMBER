package com.recipia.member.adapter.out.persistenceAdapter.querydsl;

import com.recipia.member.adapter.in.web.dto.response.AskListResponseDto;
import com.recipia.member.config.TotalTestSupport;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

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

}