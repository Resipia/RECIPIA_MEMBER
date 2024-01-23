package com.recipia.member.domain.converter;

import com.recipia.member.adapter.in.web.dto.request.UpdateMyPageRequestDto;
import com.recipia.member.adapter.in.web.dto.response.MyPageViewResponseDto;
import com.recipia.member.common.utils.SecurityUtils;
import com.recipia.member.domain.MyPage;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * dto, entity와 domain을 변환해주는 로직과
 * domain을 entity로 변환해주는 로직을 담당
 */
@RequiredArgsConstructor
@Component
public class MyPageConverter {

    private final SecurityUtils securityUtils;

    public MyPageViewResponseDto domainToResponseDto(MyPage myPage) {
        return MyPageViewResponseDto.of(
                myPage.getMemberId(),
                myPage.getImagePreUrl(),
                myPage.getNickname(),
                myPage.getIntroduction(),
                myPage.getFollowingCount(),
                myPage.getFollowerCount(),
                myPage.getBirth(),
                myPage.getGender(),
                myPage.getFollowId()
        );
    }

    public MyPage updateRequestDtoToDomain(UpdateMyPageRequestDto dto) {
        return MyPage.of(
                securityUtils.getCurrentMemberId(),
                dto.getNickname(),
                dto.getIntroduction(),
                dto.getDeleteFileOrder(),
                dto.getBirth(),
                dto.getGender()
        );
    }

}
