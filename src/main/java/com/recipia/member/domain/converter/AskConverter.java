package com.recipia.member.domain.converter;

import com.recipia.member.adapter.in.web.dto.request.AskRequestDto;
import com.recipia.member.adapter.out.persistence.AskEntity;
import com.recipia.member.adapter.out.persistence.MemberEntity;
import com.recipia.member.common.utils.SecurityUtils;
import com.recipia.member.domain.Ask;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * dto, entity와 domain을 변환해주는 로직과
 * domain을 entity로 변환해주는 로직을 담당
 */
@RequiredArgsConstructor
@Component
public class AskConverter {

    private final SecurityUtils securityUtils;

    public AskEntity domainToEntity(Ask domain) {
        MemberEntity memberEntity = MemberEntity.of(domain.getMemberId());
        return AskEntity.of(memberEntity, domain.getTitle(), domain.getContent());
    }

    public Ask dtoToDomain(AskRequestDto dto) {
        Long memberId = securityUtils.getCurrentMemberId();
        return Ask.of(memberId, dto.getTitle(), dto.getContent());
    }

}
