package com.recipia.member.application.service;

import com.recipia.member.adapter.in.web.dto.response.AskListResponseDto;
import com.recipia.member.adapter.in.web.dto.response.PagingResponseDto;
import com.recipia.member.application.port.in.AskUseCase;
import com.recipia.member.application.port.out.port.AskPort;
import com.recipia.member.common.utils.SecurityUtils;
import com.recipia.member.domain.Ask;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 문의사항 서비스 클래스
 */
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class AskService implements AskUseCase {

    private final AskPort askPort;
    private final SecurityUtils securityUtils;

    /**
     * [CREATE] 문의사항 등록하기
     */
    @Transactional
    @Override
    public Long createAsk(Ask ask) {
        return askPort.createAsk(ask);
    }

    /**
     * [READ] 문의사항 목록을 가져온다.
     */
    @Override
    public PagingResponseDto<AskListResponseDto> getAskList(int page, int size) {
        // 정렬조건을 정한 뒤 Pageable 객체 생성
        Pageable pageable = PageRequest.of(page, size);
        // 본인 id 추출
        Long memberId = securityUtils.getCurrentMemberId();

        // 데이터 가져오기
        Page<AskListResponseDto> askList = askPort.getAskList(memberId, pageable);

        return PagingResponseDto.of(askList.getContent(), askList.getTotalElements());
    }
}
