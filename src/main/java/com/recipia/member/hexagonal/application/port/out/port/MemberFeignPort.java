package com.recipia.member.hexagonal.application.port.out.port;

import com.recipia.member.hexagonal.adapter.in.feign.dto.NicknameDto;

public interface MemberFeignPort {
    NicknameDto getNicknameByMemberId(Long memberId);
}
