package com.recipia.member.adapter.out.persistenceAdapter;

import com.recipia.member.adapter.in.web.dto.response.AskListResponseDto;
import com.recipia.member.adapter.out.persistence.AskEntity;
import com.recipia.member.adapter.out.persistenceAdapter.querydsl.AskQueryRepository;
import com.recipia.member.application.port.out.port.AskPort;
import com.recipia.member.common.exception.ErrorCode;
import com.recipia.member.common.exception.MemberApplicationException;
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
     * [READ] 내가 작성한 문의내역을 가져온다.
     */
    @Override
    public Page<AskListResponseDto> getAskList(Long memberId, Pageable pageable) {
        return askQueryRepository.getAskList(memberId, pageable);
    }

    /**
     * [READ] 문의사항 상세 내용을 가져온다.
     */
    @Override
    public Ask getAskDetail(Ask doamin) {
        Ask askDetail = askQueryRepository.getAskDetail(doamin);
        // id로 문의사항을 찾을 수 없다면 에러 발생
        if (null == askDetail) {
            throw new MemberApplicationException(ErrorCode.ASK_NOT_FOUND);
        }
        return askDetail;
    }
}
