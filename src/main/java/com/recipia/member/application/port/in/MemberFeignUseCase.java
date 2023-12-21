package com.recipia.member.application.port.in;

import com.recipia.member.adapter.in.feign.dto.NicknameDto;

public interface MemberFeignUseCase {

    NicknameDto getNicknameByMemberId(Long memberId);
}
