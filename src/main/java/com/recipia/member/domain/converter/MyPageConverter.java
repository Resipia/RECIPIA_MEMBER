package com.recipia.member.domain.converter;

import com.recipia.member.adapter.in.web.dto.request.UpdateMyPageRequestDto;
import com.recipia.member.adapter.in.web.dto.response.MyPageViewResponseDto;
import com.recipia.member.common.utils.SecurityUtils;
import com.recipia.member.domain.MyPage;
import org.springframework.stereotype.Component;

/**
 * dto, entity와 domain을 변환해주는 로직과
 * domain을 entity로 변환해주는 로직을 담당
 */
@Component
public class MyPageConverter {

    public MyPageViewResponseDto domainToResponseDto(MyPage myPage) {
        return MyPageViewResponseDto.of(
                myPage.getMemberId(),
                myPage.getNickname(),
                myPage.getIntroduction(),
                myPage.getFollowingCount(),
                myPage.getFollowerCount()
        );
    }

    public MyPage updateRequestDtoToDomain(UpdateMyPageRequestDto dto) {
        return MyPage.of(
                SecurityUtils.getCurrentMemberId(),
                dto.getNickname(),
                dto.getIntroduction()
        );
    }

}
