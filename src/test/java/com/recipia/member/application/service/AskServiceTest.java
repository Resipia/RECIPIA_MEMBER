package com.recipia.member.application.service;

import com.recipia.member.adapter.in.web.dto.response.AskListResponseDto;
import com.recipia.member.adapter.in.web.dto.response.PagingResponseDto;
import com.recipia.member.application.port.out.port.AskPort;
import com.recipia.member.common.utils.SecurityUtils;
import com.recipia.member.domain.Ask;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("[단위] 문의사항 서비스 클래스")
class AskServiceTest {

    @InjectMocks
    private AskService sut;
    @Mock
    private AskPort askPort;
    @Mock
    private SecurityUtils securityUtils;

    @DisplayName("[happy] 제목과 내용이 전부 들어왔을때 등록에 성공한다.")
    @Test
    void createAsk() {
        // given
        Ask ask = Ask.of(1L, "title", "content");
        when(askPort.createAsk(ask)).thenReturn(1L);
        // when
        Long createdId = sut.createAsk(ask);
        // then
        assertEquals(createdId, 1L);
    }

    @DisplayName("[happy] 내가 작성한 문의사항 목록을 가져온다.")
    @Test
    void getAskList() {
        // given
        int page = 0;
        int size = 10;
        Long memberId = 1L;

        List<AskListResponseDto> askList = List.of(AskListResponseDto.of(1L, "title", false, "2020-11-11"),
                AskListResponseDto.of(2L, "title2", true, "2020-11-13"));
        Page<AskListResponseDto> mockPage = new PageImpl<>(askList);
        Pageable pageable = PageRequest.of(page, size);
        when(securityUtils.getCurrentMemberId()).thenReturn(memberId);
        when(askPort.getAskList(memberId, pageable)).thenReturn(mockPage);

        // when
        PagingResponseDto<AskListResponseDto> result = sut.getAskList(page, size);
        // then
        assertThat(result.getContent()).isNotEmpty();
        assertThat(result.getTotalCount()).isEqualTo(2L);
    }

}