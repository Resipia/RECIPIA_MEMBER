package com.recipia.member.domain.converter;

import com.recipia.member.adapter.in.web.dto.request.FollowRequestDto;
import com.recipia.member.common.utils.SecurityUtils;
import com.recipia.member.domain.Follow;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * dto, entity와 domain을 변환해주는 로직과
 * domain을 entity로 변환해주는 로직을 담당
 */
@RequiredArgsConstructor
@Component
public class FollowConverter {

    private final SecurityUtils securityUtils;

    public Follow requestDtoToDomain(FollowRequestDto dto) {
        Long currentMemberId = securityUtils.getCurrentMemberId();
        return Follow.of(currentMemberId, dto.getFollowingMemberId());
    }
}
