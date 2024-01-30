package com.recipia.member.adapter.out.persistenceAdapter;

import com.recipia.member.adapter.in.web.dto.response.AskListResponseDto;
import com.recipia.member.adapter.out.persistence.AskEntity;
import com.recipia.member.adapter.out.persistenceAdapter.querydsl.AskQueryRepository;
import com.recipia.member.application.port.out.port.AskPort;
import com.recipia.member.domain.Ask;
import com.recipia.member.domain.converter.AskConverter;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class AskAdapter implements AskPort {

    private final AskConverter askConverter;
    private final AskRepository askRepository;
    private final AskQueryRepository askQueryRepository;

    /**
     * [CREATE] 문의사항을 저장한다.
     */
    @Override
    public Long createAsk(Ask ask) {
        AskEntity askEntity = askConverter.domainToEntity(ask);
        return askRepository.save(askEntity).getId();
    }

    /**
     * [READ] 내가 작성한 문의 목록 가져오기
     */
    @Override
    public Page<AskListResponseDto> getAskList(Long memberId, Pageable pageable) {
        return askQueryRepository.getAskList(memberId, pageable);
    }
}
