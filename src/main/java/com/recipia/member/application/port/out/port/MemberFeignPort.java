package com.recipia.member.application.port.out.port;

import com.recipia.member.adapter.in.feign.dto.NicknameDto;

public interface MemberFeignPort {
    NicknameDto getNicknameByMemberId(Long memberId);
}
