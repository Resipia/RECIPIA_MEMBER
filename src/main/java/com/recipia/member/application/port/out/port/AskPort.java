package com.recipia.member.application.port.out.port;

import com.recipia.member.adapter.in.web.dto.response.AskListResponseDto;
import com.recipia.member.domain.Ask;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface AskPort {
    Long createAsk(Ask ask);

    Page<AskListResponseDto> getAskList(Long memberId, Pageable pageable);

    Ask getAskDetail(Ask ask);
}
