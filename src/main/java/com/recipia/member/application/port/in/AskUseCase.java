package com.recipia.member.application.port.in;

import com.recipia.member.adapter.in.web.dto.response.AskListResponseDto;
import com.recipia.member.adapter.in.web.dto.response.PagingResponseDto;
import com.recipia.member.domain.Ask;

public interface AskUseCase {
    Long createAsk(Ask ask);
    PagingResponseDto<AskListResponseDto> getAskList(int page, int size);
}
