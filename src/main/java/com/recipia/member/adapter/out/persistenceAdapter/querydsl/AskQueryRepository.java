package com.recipia.member.adapter.out.persistenceAdapter.querydsl;

import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.recipia.member.adapter.in.web.dto.response.AskListResponseDto;
import com.recipia.member.domain.Ask;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Repository;

import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

import static com.recipia.member.adapter.out.persistence.QAskEntity.askEntity;

@RequiredArgsConstructor
@Repository
public class AskQueryRepository {

    private final JPAQueryFactory jpaQueryFactory;

    /**
     * [READ] memberId에 해당하는 문의 내역 목록을 가져온다.
     */
    public Page<AskListResponseDto> getAskList(Long memberId, Pageable pageable) {
        List<AskListResponseDto> result = jpaQueryFactory
                .select(Projections.constructor(AskListResponseDto.class,
                        askEntity.id,
                        askEntity.title,
                        askEntity.createDateTime.ne(askEntity.updateDateTime),
                        Expressions.stringTemplate("TO_CHAR({0}, 'YYYY-MM-DD')", askEntity.createDateTime)
                        ))
                .from(askEntity)
                .where(askEntity.memberEntity.id.eq(memberId))
                .orderBy(askEntity.createDateTime.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        Long totalCount = Optional.ofNullable(jpaQueryFactory
                .select(askEntity.id.count())
                .from(askEntity)
                .where(askEntity.memberEntity.id.eq(memberId))
                .fetchOne()
        ).orElse(0L);

        return new PageImpl<>(result, pageable, totalCount);
    }

    /**
     * [READ] askId에 해당하는 문의사항 상세내용을 가져온다.
     */
    public Ask getAskDetail(Ask domain) {
        return jpaQueryFactory
                .select(Projections.constructor(Ask.class,
                        askEntity.id,
                        askEntity.memberEntity.id,
                        askEntity.title,
                        askEntity.content,
                        askEntity.answer,
                        Expressions.stringTemplate("TO_CHAR({0}, 'YYYY-MM-DD')", askEntity.createDateTime),
                        Expressions.stringTemplate("TO_CHAR({0}, 'YYYY-MM-DD')", askEntity.updateDateTime)
                ))
                .from(askEntity)
                .where(askEntity.id.eq(domain.getId()), askEntity.memberEntity.id.eq(domain.getMemberId()))
                .fetchOne();
    }
}
